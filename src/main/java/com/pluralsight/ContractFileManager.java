package com.pluralsight.data;

import com.pluralsight.contracts.Contract;
import com.pluralsight.contracts.SalesContract;
import com.pluralsight.contracts.LeaseContract;
import com.pluralsight.model.Car;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;


public class ContractFileManager {

    private final String contractsPath;
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public ContractFileManager(String contractsPath) {
        this.contractsPath = contractsPath;
    }

    public void saveContract(Contract c) {
        String line = (c instanceof SalesContract)
                ? buildSaleLine((SalesContract) c)
                : buildLeaseLine((LeaseContract) c);

        try (FileWriter fw = new FileWriter(contractsPath, true)) { // append = true
            fw.write(line);
            if (!line.endsWith("\n")) fw.write("\n");
        } catch (IOException e) {
            throw new RuntimeException("Failed to append contract to " + contractsPath, e);
        }
    }


    private String buildCommonPrefix(String type, Contract c) {
        Car v = c.getCar();
        return String.join("|",
                type,
                nz(c.getDate()),
                nz(c.getCustomerName()),
                nz(c.getCustomerEmail()),
                String.valueOf(v.getVin()),
                String.valueOf(v.getYear()),
                nz(v.getMake()),
                nz(v.getModel()),
                nz(v.getVehicleType()),
                nz(v.getColor()),
                String.valueOf(v.getOdometer()),
                DF.format(v.getPrice())
        );
    }

    private String buildSaleLine(SalesContract s) {
        String prefix = buildCommonPrefix("SALE", s);
        String tax = DF.format(s.getSalesTaxAmount());
        String rec = DF.format(s.getRecordingFee());
        String proc = DF.format(s.getProcessingFee());
        String total = DF.format(s.getTotalPrice());
        String fin  = s.isFinanced() ? "YES" : "NO";
        String monthly = DF.format(s.getMonthlyPayment());
        return String.join("|", prefix, tax, rec, proc, total, fin, monthly);
    }

    private String buildLeaseLine(LeaseContract l) {
        String prefix = buildCommonPrefix("LEASE", l);
        String end = DF.format(l.getExpectedEndingValue());
        String fee = DF.format(l.getLeaseFee());
        String total = DF.format(l.getTotalPrice());
        String monthly = DF.format(l.getMonthlyPayment());
        return String.join("|", prefix, end, fee, total, monthly);
    }

    private static String nz(String s) { return s == null ? "" : s; }
}
