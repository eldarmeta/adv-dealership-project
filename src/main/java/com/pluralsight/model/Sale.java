package com.pluralsight.model;

public class Sale {
    private final String saleId;
    private final String vin;
    private final String customerId;
    private final String salespersonId;
    private final double finalPrice;
    private final String dateTimeIso;

    public Sale(String saleId, String vin, String customerId, String salespersonId, double finalPrice, String dateTimeIso) {
        this.saleId = saleId;
        this.vin = vin;
        this.customerId = customerId;
        this.salespersonId = salespersonId;
        this.finalPrice = finalPrice;
        this.dateTimeIso = dateTimeIso;
    }

    public String getSaleId() { return saleId; }
    public String getVin() { return vin; }
    public String getCustomerId() { return customerId; }
    public String getSalespersonId() { return salespersonId; }
    public double getFinalPrice() { return finalPrice; }
    public String getDateTimeIso() { return dateTimeIso; }
}
