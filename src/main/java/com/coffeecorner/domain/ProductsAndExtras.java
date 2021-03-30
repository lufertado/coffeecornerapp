package com.coffeecorner.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsAndExtras {

    public static Map<Integer, Product> products = new HashMap<>();
    public static Map<Integer, Extra> extras = new HashMap<>();

    static {
        products.put(1, new Product(1, "Coffee - Small", 2.5f));
        products.put(2, new Product(2, "Coffee - Medium", 3f));
        products.put(3, new Product(3, "Coffee - Large", 3.5f));
        products.put(4, new Product(4, "Bacon Roll", 4.5f));
        products.put(5, new Product(5, "Squeezed orange juice", 3.95f));

        extras.put(1, new Extra(1, "Extra milk", 0.3f));
        extras.put(2, new Extra(2, "Foamed milk", 0.5f));
        extras.put(3, new Extra(3, "Special roast coffee", 0.9f));
    }

    public static boolean isASnack(Integer productCode) {
        return productCode == 4;
    }

    public static boolean isACoffee(Integer productCode) {
        return productCode == 1 || productCode == 2 || productCode == 3;
    }
}
