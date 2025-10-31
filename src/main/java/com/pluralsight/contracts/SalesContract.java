package com.pluralsight.contracts;

import  com.pluralsight.model.Car;

public class SalesContract extends  Contract {
    private static final double SALES_TAX_RATE = 0.05;
    private static final double RECORDING_FEE = 100.0;

    private final double processingFee;
    private final boolean financed;

    public SalesContract(String date, String customerName, String customerEmail,
                         Car car, boolean financed) {
        super(date, customerName, customerEmail, car);
        this.processingFee = car.getPrice() < 10_000 ? 295.0 : 495.0;
        this.financed = financed;
    }

    @Override
    public double getTotalPrice() {
        double base = getCar().getPrice();
        double tax = base * SALES_TAX_RATE;
        return base + tax + RECORDING_FEE + processingFee;
    }
    @Override
    public double getMonthlyPaymant() {
        if (!financed) return 0.0;

        double financedAmount = getTotalPrice();
        boolean isBig = getCar().getPrice() >= 10_000;

        double apr = is Big ? 0.0425 : 0.0525;
        int months = isBig ? 48 : 24;
        double r = apr / 12.0;

        return  financedAmount * r / (1 - Math.pow(1 + r, -months));
    }

    public boolean isFinanced() {
        return financed;
    }
    public double getProcessingFee() {
        return processingFee;
    }
    public double getRecordingFee() {
        return RECORDING_FEE;
    }
    public  double getSalesTaxRate() {
        return  getCar().getPrice() * SALES_TAX_RATE;
    }
}
