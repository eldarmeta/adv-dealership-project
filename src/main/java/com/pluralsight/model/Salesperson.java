package com.pluralsight.model;

public class Salesperson {
    private final String id;
    private final String name;

    public Salesperson(String id, String name) {
        this.id = id; this.name = name;
    }
    public String getId() { return id; }
    public String getName() { return name; }
}
