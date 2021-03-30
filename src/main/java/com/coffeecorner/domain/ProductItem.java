package com.coffeecorner.domain;

import java.util.ArrayList;
import java.util.List;

public class ProductItem {
    private final String name;
    private final int quantity;
    private final double price;
    private List<ExtraItem> extras = new ArrayList<>();

    public ProductItem(String name, int quantity, double price, List<ExtraItem> extras) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.extras = extras;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public List<ExtraItem> getExtras() {
        return extras;
    }
}
