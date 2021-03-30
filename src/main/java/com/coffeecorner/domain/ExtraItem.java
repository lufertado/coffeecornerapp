package com.coffeecorner.domain;

public class ExtraItem {
    private final String name;
    private final double price;

    public ExtraItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
