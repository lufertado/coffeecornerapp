package com.coffeecorner.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.coffeecorner.domain.DiscountItem;
import com.coffeecorner.domain.Extra;
import com.coffeecorner.domain.ExtraItem;
import com.coffeecorner.domain.Product;
import com.coffeecorner.domain.ProductCode;
import com.coffeecorner.domain.ProductItem;
import com.coffeecorner.domain.ProductsAndExtras;

public class InvoiceService {

    public List<ProductItem> generateInvoiceDetail(List<ProductCode> productCodes) {
        List<ProductItem> items = new ArrayList<>();
        Map<ProductCode, Integer> itemsByProductCode = itemsByProductCode(productCodes);
        for (ProductCode productCode : itemsByProductCode.keySet()) {
            Product product = ProductsAndExtras.products.get(productCode.getCode());
            Integer numOfItems = itemsByProductCode.get(productCode);
            double price = product.getPrice() * numOfItems;
            List<ExtraItem> itemExtras = itemExtras(productCode);
            items.add(new ProductItem(product.getName(), numOfItems, price, itemExtras));
        }
        return items;
    }

    private List<ExtraItem> itemExtras(ProductCode productCode) {
        List<ExtraItem> itemExtras = new ArrayList<>();
        if(ProductsAndExtras.isACoffee(productCode.getCode())) {
            for (Integer extraCode : productCode.getExtraCodes()) {
                Extra extra = ProductsAndExtras.extras.get(extraCode);
                double extraPrice = extra.getPrice();
                itemExtras.add(new ExtraItem(extra.getName(), extraPrice));
            }
        }
        return itemExtras;
    }

    public List<DiscountItem> generateDiscounts(List<ProductCode> itemsByProductCode, int stamps) {
        List<DiscountItem> discountItems = new ArrayList<>();
        Optional<Integer> optCoffeeProdCode = itemsByProductCode.stream()
                                                                .map(ProductCode::getCode)
                                                                .filter(ProductsAndExtras::isACoffee)
                                                                .findFirst();
        if(optCoffeeProdCode.isPresent()) {
            if(stamps == 5) {
                int coffeeProdCode = optCoffeeProdCode.get();
                double discount = ProductsAndExtras.products.get(coffeeProdCode).getPrice();
                discountItems.add(new DiscountItem("Stamps discount (1 Coffee):                 ", discount));
            }
            Optional<Integer> optSnackProdCode = itemsByProductCode.stream()
                                                                   .map(ProductCode::getCode)
                                                                   .filter(ProductsAndExtras::isASnack)
                                                                   .findFirst();
            if(optSnackProdCode.isPresent()) {
                Optional<ProductCode> optCoffeeWithExtraCode = itemsByProductCode.stream()
                                                                                 .filter(productCode -> ProductsAndExtras.isACoffee(productCode.getCode())
                                                                                                        && !productCode.getExtraCodes().isEmpty())
                                                                                 .findFirst();

                if(optCoffeeWithExtraCode.isPresent()) {
                    double discount = ProductsAndExtras.extras.get(optCoffeeWithExtraCode.get().getExtraCodes().get(0)).getPrice();
                    discountItems.add(new DiscountItem("Combo Coffee & Snack discount (1 Extra):    ", discount));
                }
            }
        }
        return discountItems;
    }

    public Map<ProductCode, Integer> itemsByProductCode(List<ProductCode> productCodes) {
        Map<ProductCode, Integer> itemsByProductCode = new HashMap<>();
        for (ProductCode productCode : productCodes) {
            Integer numOfItems = itemsByProductCode.get(productCode);
            itemsByProductCode.put(productCode, numOfItems == null ? 1 : ++numOfItems);
        }
        return itemsByProductCode;
    }

    public double calculateTotalPrice(List<ProductItem> items, List<DiscountItem> discountItems) {
        double total = items.stream().mapToDouble(ProductItem::getPrice).sum();
        total += items.stream().flatMap(productItem -> productItem.getExtras().stream()).mapToDouble(ExtraItem::getPrice).sum();
        total -= discountItems.stream().mapToDouble(DiscountItem::getPrice).sum();
        return total;
    }
}
