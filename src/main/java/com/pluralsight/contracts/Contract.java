package com.pluralsight.contracts;

import com.pluralsight.model.Car;

public abstract class Contract {
    private final String date;
    private final String customerName;
    private final String customerEmail;
    private final Car car;

    protected Contract(String date, String customerName, String customerEmail, Car car) {
        this.date = date;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.car = car;
    }

    public String getDate() { return date; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public Car getCar() { return car; }

    public abstract double getTotalPrice();
    public abstract double getMonthlyPayment();
}
