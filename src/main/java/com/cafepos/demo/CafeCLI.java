package com.cafepos.demo;

import com.cafepos.catalog.Product;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.factory.ProductFactory;

import java.util.Scanner;

public final class CafeCLI {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProductFactory factory = new ProductFactory();
        Order order = new Order(OrderIds.next());

        System.out.println("☕ Welcome to CafePOS CLI ☕");
        System.out.println("Type your drink recipes using codes like:");
        System.out.println("  ESP, LAT, CAP for base drinks");
        System.out.println("  +SHOT, +OAT, +SYP, +L for addons");
        System.out.println("Example: LAT+OAT+L");
        System.out.println("Type 'done' to finish your order.\n");

        while (true) {
            System.out.print("Enter recipe (or 'done'): ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("done")) break;
            if (input.isBlank()) continue;

            try {
                Product product = factory.create(input);
                System.out.print("Quantity: ");
                int qty = Integer.parseInt(sc.nextLine().trim());
                order.addItem(new LineItem(product, qty));
                System.out.println("Added: " + product.name() + " x" + qty);
                System.out.println("Current subtotal: " + order.subtotal());
                System.out.println();
            } catch (Exception e) {
                System.out.println("⚠️ Error: " + e.getMessage());
            }
        }

        System.out.println("\n=== Order Summary ===");
        System.out.println("Order #" + order.id());
        for (LineItem li : order.items()) {
            System.out.println(" - " + li.product().name() + " x" + li.quantity() + " = " + li.lineTotal());
        }

        System.out.println("---------------------------");
        System.out.println("Subtotal: " + order.subtotal());
        System.out.println("Tax (10%): " + order.taxAtPercent(10));
        System.out.println("Total: " + order.totalWithTax(10));
        System.out.println("\n✅ Thank you! Your order is complete.");
    }
}
