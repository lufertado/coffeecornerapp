package com.coffeecorner;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import com.coffeecorner.domain.DiscountItem;
import com.coffeecorner.domain.ProductCode;
import com.coffeecorner.domain.ProductItem;
import com.coffeecorner.domain.ProductsAndExtras;
import com.coffeecorner.printer.InvoicePrinter;
import com.coffeecorner.service.InvoiceService;

public class CoffeeCornerApplication {

    private final InvoiceService service = new InvoiceService();
    private final InvoicePrinter printer = new InvoicePrinter();

    public static void main(String... args) {
        new CoffeeCornerApplication().generateInvoice();
    }

    public void generateInvoice() {
        System.out.println(
            "WELCOME TO CHARLENE'S COFFEE CORNER.\n\n\n" +
            "This is the menu:\n\n" +

            "CODE       NAME                        PRICE(U)\n" +
            "1          Coffee - Small              2.50 CHF\n" +
            "2          Coffee - Medium             3 CHF\n" +
            "3          Coffee - Large              3.5 CHF\n" +
            "4          Bacon Roll                  4.50 CHF\n" +
            "5          Squeezed orange juice       3.95 CHF\n\n" +
            "This are the extras:\n\n" +

            "CODE       NAME                        PRICE(U)\n" +
            "1          Extra milk                  2.50 CHF\n" +
            "2          Foamed milk                 3 CHF\n" +
            "3          Special roast coffee        3.5 CHF\n");

        System.out.println("Please enter the number of products to buy:");
        Scanner scanner = new Scanner(System.in);
        int numberOfProducts = scanner.nextInt();
        System.out.println("For each product, please enter the number in the list, then press ENTER:");
        List<ProductCode> products = new ArrayList<>();
        int counter = numberOfProducts;
        while (counter > 0) {
            System.out.printf("Next product code (%s remaining):%n", counter);
            int productCode = scanner.nextInt();
            if(productCode < 1 || productCode > 5) {
                System.out.println("Please enter a valid product code:");
            }
            else {
                List<Integer> extraCodes = new ArrayList<>();
                if(ProductsAndExtras.isACoffee(productCode)) {
                    System.out.println("Do you want extras for this coffee? (Y/N)");
                    String hasExtras = scanner.next();
                    while(!hasExtras.equalsIgnoreCase("y") && !hasExtras.equalsIgnoreCase("n")) {
                        System.out.println("Please enter 'Y' or 'N':");
                        hasExtras = scanner.next();
                    }
                    if(hasExtras.equalsIgnoreCase("y")) {
                        System.out.println("Please enter the extras codes separated by comma:");
                        extraCodes = parseExtraCodes(scanner.next());
                        while (extraCodes == null) {
                            System.out.println("Please enter a valid extra code:");
                            extraCodes = parseExtraCodes(scanner.next());
                        }
                    }
                }
                products.add(new ProductCode(productCode, extraCodes));
                counter--;
            }
        }
        int stamps = 0;
        System.out.println("Do you have a customer stamp card? (Y/N)");
        String hasStampCard = scanner.next();
        while(!hasStampCard.equalsIgnoreCase("y") && !hasStampCard.equalsIgnoreCase("n")) {
            System.out.println("Please enter 'Y' or 'N':");
            hasStampCard = scanner.next();
        }
        if(hasStampCard.equalsIgnoreCase("y")) {
            System.out.println("How many stamps it has (1 to 5)?");
            stamps = scanner.nextInt();
            while(stamps < 0 || stamps > 5) {
                System.out.println("Please enter a number between 1 and 5");
                stamps = scanner.nextInt();
            }
        }
        try {
            List<ProductItem> items = service.generateInvoiceDetail(products);
            List<DiscountItem> discountItems = service.generateDiscounts(products, stamps);
            double total = service.calculateTotalPrice(items, discountItems);
            printer.printInvoice(items, discountItems, total);
        } catch (FileNotFoundException e) {
            System.out.println("Error writing invoice to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Integer> parseExtraCodes(String extraCodesStr) {
        try {
            return Arrays.stream(extraCodesStr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
}