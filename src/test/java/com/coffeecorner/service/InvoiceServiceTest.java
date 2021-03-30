package com.coffeecorner.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import com.coffeecorner.domain.DiscountItem;
import com.coffeecorner.domain.ProductCode;
import com.coffeecorner.domain.ProductItem;

public class InvoiceServiceTest {

    private final InvoiceService service = new InvoiceService();

    @Test
    public void testInvoiceDetails() {
        List<ProductCode> productCodes = Arrays.asList(
            new ProductCode(1),
            new ProductCode(1, Arrays.asList(2, 3)),
            new ProductCode(4),
            new ProductCode(3, Collections.singletonList(1)),
            new ProductCode(2, Arrays.asList(1, 2, 3))
        );

        List<ProductItem> items = service.generateInvoiceDetail(productCodes);
        Assert.assertEquals(4, items.size());
        Assert.assertTrue(items.stream().anyMatch(item ->
                                                      item.getName().equals("Coffee - Small") &&
                                                      item.getQuantity() == 2 &&
                                                      item.getPrice() == 5
        ));
        Assert.assertTrue(items.stream().anyMatch(item ->
                                                      item.getName().equals("Coffee - Medium") &&
                                                      item.getQuantity() == 1 &&
                                                      item.getPrice() == 3
        ));
        Assert.assertTrue(items.stream().anyMatch(item ->
                                                      item.getName().equals("Coffee - Large") &&
                                                      item.getQuantity() == 1 &&
                                                      item.getPrice() == 3.5
        ));
        Assert.assertTrue(items.stream().anyMatch(item ->
                                                      item.getName().equals("Bacon Roll") &&
                                                      item.getQuantity() == 1 &&
                                                      item.getPrice() == 4.5
        ));
    }

    @Test
    public void testInvoiceDiscounts() {
        List<ProductCode> productCodes = Arrays.asList(
            new ProductCode(1),
            new ProductCode(1, Arrays.asList(2, 3)),
            new ProductCode(4),
            new ProductCode(3, Collections.singletonList(1)),
            new ProductCode(2, Arrays.asList(1, 2, 3))
        );
        int stamps = 5;

        List<DiscountItem> discounts = service.generateDiscounts(productCodes, stamps);
        Assert.assertEquals(2, discounts.size());
        Assert.assertTrue(discounts.stream().anyMatch(item ->
                                                      item.getName().equals("Stamps discount (1 Coffee):                 ")&&
                                                      item.getPrice() == 2.5
        ));
        Assert.assertTrue(discounts.stream().anyMatch(item ->
                                                      item.getName().equals("Combo Coffee & Snack discount (1 Extra):    ") &&
                                                      item.getPrice() == 0.5
        ));
    }

    @Test
    public void testInvoiceTotal() {
        List<ProductCode> productCodes = Arrays.asList(
            new ProductCode(1),
            new ProductCode(1, Arrays.asList(2, 3)),
            new ProductCode(4),
            new ProductCode(3, Collections.singletonList(1)),
            new ProductCode(2, Arrays.asList(1, 2, 3))
        );
        int stamps = 5;

        List<ProductItem> items = service.generateInvoiceDetail(productCodes);
        List<DiscountItem> discounts = service.generateDiscounts(productCodes, stamps);
        double total = service.calculateTotalPrice(items, discounts);
        Assert.assertEquals(15, total, 0);
    }
}
