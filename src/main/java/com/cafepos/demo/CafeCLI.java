package com.cafepos.demo;

import java.util.Scanner;

import com.cafepos.catalog.Product;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.factory.ProductFactory;
import com.cafepos.payment.CardPayment;
import com.cafepos.payment.CashPayment;
import com.cafepos.payment.PaymentStrategy;
import com.cafepos.payment.WalletPayment;

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
        
        // Payment selection
        System.out.println("\n=== Payment Options ===");
        System.out.println("1. Cash");
        System.out.println("2. Card");
        System.out.println("3. Wallet");
        
        PaymentStrategy payment = null;
        while (payment == null) {
            System.out.print("Select payment method (1-3): ");
            String paymentChoice = sc.nextLine().trim();
            
            switch (paymentChoice) {
                case "1":
                    payment = new CashPayment();
                    break;
                case "2":
                    System.out.print("Enter card number: ");
                    String cardNumber = sc.nextLine().trim();
                    if (!cardNumber.isEmpty()) {
                        payment = new CardPayment(cardNumber);
                    } else {
                        System.out.println("⚠️ Card number cannot be empty. Please try again.");
                    }
                    break;
                case "3":
                    System.out.print("Enter wallet ID: ");
                    String walletId = sc.nextLine().trim();
                    if (!walletId.isEmpty()) {
                        payment = new WalletPayment(walletId);
                    } else {
                        System.out.println("⚠️ Wallet ID cannot be empty. Please try again.");
                    }
                    break;
                default:
                    System.out.println("⚠️ Invalid choice. Please select 1, 2, or 3.");
            }
        }
        
        // Process payment
        payment.pay(order);
        System.out.println("\n✅ Thank you! Your order is complete.");
    }
}
