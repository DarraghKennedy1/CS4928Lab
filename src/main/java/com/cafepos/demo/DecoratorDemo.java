package com.cafepos.demo;

import com.cafepos.catalog.*;
import com.cafepos.common.Money;
import com.cafepos.domain.SimpleProduct;
import com.cafepos.domain.Order;
import com.cafepos.domain.LineItem;

public final class DecoratorDemo {
    public static void main(String[] args) {
        // Step 3: Demonstrate decorator stacking
        System.out.println("=== Decorator Stacking Demo ===");
        
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        System.out.println("Base: " + espresso.name() + " - $" + ((Priced) espresso).price());
        
        // Single decorator
        Product withShot = new ExtraShot(espresso);
        System.out.println("With Extra Shot: " + withShot.name() + " - $" + ((Priced) withShot).price());
        
        // Multiple decorators stacked
        Product decorated = new SizeLarge(new OatMilk(new ExtraShot(espresso)));
        System.out.println("Fully Decorated: " + decorated.name() + " - $" + ((Priced) decorated).price());
        
        // Step 4: Demonstrate integration with orders
        System.out.println("\n=== Order Integration Demo ===");
        
        Order order = new Order(1001);
        order.addItem(new LineItem(decorated, 1));
        order.addItem(new LineItem(withShot, 2));
        
        System.out.println("Order #" + order.id());
        for (LineItem li : order.items()) {
            System.out.println(" - " + li.product().name() + " x" + li.quantity() + " = $" + li.lineTotal());
        }
        System.out.println("Subtotal: $" + order.subtotal());
        System.out.println("Tax (10%): $" + order.taxAtPercent(10));
        System.out.println("Total: $" + order.totalWithTax(10));
    }
}
