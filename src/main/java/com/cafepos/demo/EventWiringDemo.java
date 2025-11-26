package com.cafepos.demo;

import com.cafepos.app.events.EventBus;
import com.cafepos.app.events.OrderCreated;
import com.cafepos.app.events.OrderPaid;
import com.cafepos.infra.Wiring;
import com.cafepos.ui.OrderController;

/**
 * Event Wiring Demo - Components & Connectors
 * Demonstrates how EventBus decouples components via publish/subscribe.
 */
public final class EventWiringDemo {
    public static void main(String[] args) {
        var bus = new EventBus();
        var comp = Wiring.createDefault();
        var controller = new OrderController(comp.repo(), comp.checkout());

        // Subscribe to events
        bus.on(OrderCreated.class, e -> 
            System.out.println("[UI] order created: " + e.orderId())
        );
        bus.on(OrderPaid.class, e -> 
            System.out.println("[UI] order paid: " + e.orderId())
        );

        // Create order and emit events
        long id = 4201L;
        controller.createOrder(id);
        bus.emit(new OrderCreated(id));

        // Simulate payment
        bus.emit(new OrderPaid(id));
    }
}
