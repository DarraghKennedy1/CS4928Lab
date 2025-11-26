package com.cafepos.demo;

import com.cafepos.infra.Wiring;
import com.cafepos.ui.ConsoleView;
import com.cafepos.ui.OrderController;

/**
 * Week 10 Demo - MVC Pattern
 * Demonstrates Model-View-Controller separation in the Presentation layer.
 */
public final class Week10Demo_MVC {
    public static void main(String[] args) {
        // Wire dependencies
        var c = Wiring.createDefault();
        var controller = new OrderController(c.repo(), c.checkout());
        var view = new ConsoleView();

        // Create order and add items
        long id = 4101L;
        controller.createOrder(id);
        controller.addItem(id, "ESP+SHOT+OAT", 1);
        controller.addItem(id, "LAT+L", 2);

        // Checkout and display receipt
        String receipt = controller.checkout(id, 10);
        view.print(receipt);
    }
}
