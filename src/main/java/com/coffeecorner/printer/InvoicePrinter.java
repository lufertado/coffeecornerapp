package com.coffeecorner.printer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;
import com.coffeecorner.domain.DiscountItem;
import com.coffeecorner.domain.ExtraItem;
import com.coffeecorner.domain.ProductItem;

public class InvoicePrinter {

    private PrintWriter writer;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public void printInvoice(List<ProductItem> items, List<DiscountItem> discountItems, double total) throws FileNotFoundException {
        writer = new PrintWriter(new File("invoice.txt"));
        printInvoiceHeader();
        printInvoiceDetail(items);
        printDiscounts(discountItems);
        printInvoiceTotal(total);
        writer.flush();
    }

    private void printInvoiceHeader() {
        writer.write(
            "CHARLENE'S COFFEE CORNER.\n" +
            "Invoice\n\n" +
            "------------------------------------------------------\n" +
            "Item                       Quantity        Price (CHF)\n" +
            "------------------------------------------------------\n");
    }


    private void printInvoiceDetail(List<ProductItem> items) {
        for (ProductItem item : items) {
            writer.write(item.getName());
            for(int i = 1; i <= 29 - item.getName().length(); i++) {
                printWhitespace(writer);
            }
            writer.write(Integer.toString(item.getQuantity()));
            for(int i = 1; i <= 15 - Integer.toString(item.getQuantity()).length(); i++) {
                printWhitespace(writer);
            }
            writer.write(decimalFormat.format(item.getPrice()));
            writer.write("\n");
            printExtraItems(item.getExtras());
        }
    }

    private void printExtraItems(List<ExtraItem> extraItems) {
        for (ExtraItem item : extraItems) {
            writer.write("  - ");
            writer.write(item.getName());
            for(int i = 1; i <= 40 - item.getName().length(); i++) {
                printWhitespace(writer);
            }
            writer.write(decimalFormat.format(item.getPrice()));
            writer.write("\n");
        }
    }

    private void printDiscounts(List<DiscountItem> discountItems) {
        writer.write("\n");
        for (DiscountItem discountItem : discountItems) {
            writer.write(discountItem.getName());
            for(int i = 1; i <= 17 - discountItem.getName().length(); i++) {
                printWhitespace(writer);
            }
            writer.write("-");
            writer.write(decimalFormat.format(discountItem.getPrice()));
            writer.write("\n");
        }
    }

    private void printInvoiceTotal(double total) {
        writer.write("------------------------------------------------------\n");
        writer.write("                            TOTAL:          " + decimalFormat.format(total));
    }

    private void printWhitespace(PrintWriter writer) {
        writer.write(" ");
    }
}
