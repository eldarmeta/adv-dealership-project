package com.pluralsight.model;

public class Car {
    private final String vin;      // ключ (уникален)
    private String make;
    private String model;
    private int year;
    private double price;
    private boolean sold;

    public Car(String vin, String make, String model, int year, double price, boolean sold) {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.sold = sold;
    }

    public Car(String vin, String make, String model, int year, double price) {
        this(vin, make, model, year, price, false);
    }

    public String getVin() { return vin; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public boolean isSold() { return sold; }

    public void setPrice(double price) { this.price = price; }
    public void setSold(boolean sold)   { this.sold = sold; }

    @Override
    public String toString() {
        return year + " " + make + " " + model + " (VIN: " + vin + ") $" + price + (sold ? " [SOLD]" : "");
    }
}
