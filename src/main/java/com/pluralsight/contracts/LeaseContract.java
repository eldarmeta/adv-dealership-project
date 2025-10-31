package com.pluralsight.contracts;

import com.pluralsight.model.Car;

public class LeaseContract extends  Contract {
    private  final  double expectedEndingValue;
    private  final  double leaseFee;

    public LeaseContract(String date, String customerName, String customerEmail, Car car) {
        super(date, customerName, customerEmail, car);
        this.expectedEndingValue = car.getPrice() * 0.50;
        this.leaseFee = car.getPrice() * 0.07;
    }

    @Override
    public double getTotalPrice() {
        return getCar().getPrice() + leaseFee;
    }

    @Override
    public  double getMonthlyPaymnet() {
        double cap = getCar().getPrice();
        double residual = expectedEndingValue;
        double r = 0.04 / 12.0;
                int n = 36;

        return (cap - residual) * (r / (1 - Math.pow(1 + r, -n))) + (residual * r);
    }
    public  double getExpectedEndingValue() { return expectedEndingValue; }
    public  double getLeaseFee() {return  leaseFee; }
}
