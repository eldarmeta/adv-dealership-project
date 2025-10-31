package com.pluralsight;

import com.pluralsight.model.Car;
import com.pluralsight.model.Sale;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

public class Main {

    private static final String INVENTORY = "./inventory.csv";
    private static final String SALES     = "./sales.csv";
    private static final HashMap<String, Car> inventory = new HashMap<>();
    private static final ArrayList<Sale> sales = new ArrayList<>();
    private static final String CONTRACTS = "./contracts.csv";
    private static com.pluralsight.data.ContractFileManager contractFileManager =
            new com.pluralsight.data.ContractFileManager(CONTRACTS);

    public static void main(String[] args) {
        loadInventory();
        loadSales();

        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("\nCar Dealership (CSV)");
            System.out.println("1) Add car");
            System.out.println("2) List available");
            System.out.println("3) Sell car");
            System.out.println("4) Sales report (count + total)");
            System.out.println("5) Sell/Lease vehicle");
            System.out.println("0) Exit");
            System.out.print("Choose: ");
            String c = in.nextLine().trim();

            if (c.equals("1")) addCarFlow(in);
            else if (c.equals("2")) listAvailableFlow();
            else if (c.equals("3")) sellCarFlow(in);
            else if (c.equals("4")) salesReportFlow();
            else if (c.equals("5")) sellOrLeaseFlow();
            else if (c.equals("0")) { System.out.println("Bye!"); break; }
            else System.out.println("Unknown");
        }
    }

    private static void loadInventory() {
        File f = new File(INVENTORY);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            boolean headerChecked = false;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                if (!headerChecked) {
                    headerChecked = true;
                    String lower = line.toLowerCase(Locale.ROOT);
                    if (lower.startsWith("vin,")) continue;
                }
                String[] t = line.split(",", -1);
                String vin = t[0];
                String make = t[1];
                String model = t[2];
                int year = Integer.parseInt(t[3]);
                double price = Double.parseDouble(t[4]);
                boolean sold = Boolean.parseBoolean(t[5]);
                inventory.put(vin, new Car(vin, make, model, year, price, sold));
            }
        } catch (IOException e) {
            System.out.println("Read inventory error: " + e.getMessage());
        }
    }

    private static void loadSales() {
        File f = new File(SALES);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            boolean headerChecked = false;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                if (!headerChecked) {
                    headerChecked = true;
                    String lower = line.toLowerCase(Locale.ROOT);
                    if (lower.startsWith("saleid,")) continue;
                }
                String[] t = line.split(",", -1);
                String saleId = t[0];
                String vin = t[1];
                String customerId = t[2];
                String salespersonId = t[3];
                double finalPrice = Double.parseDouble(t[4]);
                String dateTimeIso = t[5];
                sales.add(new Sale(saleId, vin, customerId, salespersonId, finalPrice, dateTimeIso));
            }
        } catch (IOException e) {
            System.out.println("Read sales error: " + e.getMessage());
        }
    }

    private static void saveInventoryAll() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(INVENTORY, false))) {
            for (Car c : inventory.values()) {
                String line = c.getVin() + "," + c.getMake() + "," + c.getModel() + ","
                        + c.getYear() + "," + c.getPrice() + "," + c.isSold();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Write inventory error: " + e.getMessage());
        }
    }

    private static void appendSaleRow(Sale s) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SALES, true))) {
            String row = s.getSaleId() + "," + s.getVin() + "," + s.getCustomerId() + ","
                    + s.getSalespersonId() + "," + s.getFinalPrice() + "," + s.getDateTimeIso();
            bw.write(row);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Append sale error: " + e.getMessage());
        }
    }

    private static void addCarFlow(Scanner in) {
        System.out.print("VIN: ");    String vin = in.nextLine().trim();
        if (vin.isEmpty() || inventory.containsKey(vin)) { System.out.println("Bad or duplicate VIN"); return; }

        System.out.print("Make: ");   String make = in.nextLine().trim();
        System.out.print("Model: ");  String model = in.nextLine().trim();
        System.out.print("Year: ");   int year = Integer.parseInt(in.nextLine().trim());
        System.out.print("Price: ");  double price = Double.parseDouble(in.nextLine().trim());

        inventory.put(vin, new Car(vin, make, model, year, price, false));
        saveInventoryAll();
        System.out.println("Added.");
    }

    private static void listAvailableFlow() {
        boolean any = false;
        for (Car c : inventory.values()) {
            if (!c.isSold()) { System.out.println(c); any = true; }
        }
        if (!any) System.out.println("No cars available.");
    }

    private static void sellCarFlow(Scanner in) {
        System.out.print("VIN: "); String vin = in.nextLine().trim();
        Car c = inventory.get(vin);
        if (c == null) { System.out.println("No such VIN"); return; }
        if (c.isSold()) { System.out.println("Already sold"); return; }

        System.out.print("Customer ID: ");    String cust = in.nextLine().trim();
        System.out.print("Salesperson ID: "); String sp   = in.nextLine().trim();
        System.out.print("Final price: ");    double p    = Double.parseDouble(in.nextLine().trim());

        c.setSold(true);
        saveInventoryAll();

        String id = UUID.randomUUID().toString();
        String when = LocalDateTime.now().toString();
        Sale s = new Sale(id, vin, cust, sp, p, when);
        sales.add(s);
        appendSaleRow(s);
        System.out.println("Sold!");
    }

    private static void salesReportFlow() {
        if (sales.isEmpty()) { System.out.println("No sales yet."); return; }
        double total = 0.0;
        for (int i = 0; i < sales.size(); i++) total += sales.get(i).getFinalPrice();
        System.out.println("Sales count: " + sales.size());
        System.out.println("Total revenue: " + total);
    }

    private static void sellOrLeaseFlow() {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter VIN: ");
        String vin = in.nextLine().trim();
        com.pluralsight.model.Car car = inventory.get(vin);
        if (car == null) {
            System.out.println("No car with this VIN in inventory.");
            return;
        }
        if (car.isSold()) {
            System.out.println("This car is already marked as sold.");
            return;
        }

        System.out.print("Date (YYYYMMDD): ");
        String date = in.nextLine().trim();

        System.out.print("Customer name: ");
        String name = in.nextLine().trim();

        System.out.print("Customer email: ");
        String email = in.nextLine().trim();

        System.out.print("Type (S=Sale, L=Lease): ");
        String type = in.nextLine().trim().toUpperCase();

        com.pluralsight.contracts.Contract contract;

        if ("S".equals(type)) {
            System.out.print("Finance? (Y/N): ");
            boolean fin = in.nextLine().trim().equalsIgnoreCase("Y");
            contract = new com.pluralsight.contracts.SalesContract(date, name, email, car, fin);
        } else if ("L".equals(type)) {
            int currentYear = java.time.LocalDate.now().getYear();
            if (currentYear - car.getYear() > 3) {
                System.out.println("Cannot lease vehicles older than 3 years.");
                return;
            }
            contract = new com.pluralsight.contracts.LeaseContract(date, name, email, car);
        } else {
            System.out.println("Unknown type. Use S or L.");
            return;
        }

        contractFileManager.saveContract(contract);
        inventory.remove(vin);
        saveInventoryAll();

        System.out.printf("Contract saved. Total: %.2f, Monthly: %.2f%n",
                contract.getTotalPrice(), contract.getMonthlyPayment());
    }
}
