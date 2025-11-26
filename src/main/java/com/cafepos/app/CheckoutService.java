package com.cafepos.app;

import com.cafepos.domain.Order;
import com.cafepos.domain.OrderRepository;

/**
 * Application service for checkout operations.
 * Coordinates domain and pricing without doing I/O.
 */
public final class CheckoutService {
    private final OrderRepository orders;
    private final PricingService pricing;

    public CheckoutService(OrderRepository orders, PricingService pricing) {
        this.orders = orders;
        this.pricing = pricing;
    }

    /**
     * Returns a receipt string; does NOT print.
     */
    public String checkout(long orderId, int taxPercent) {
        Order order = orders.findById(orderId).orElseThrow(
            () -> new IllegalArgumentException("Order not found: " + orderId)
        );
        
        var pr = pricing.price(order.subtotal());
        return new ReceiptFormatter().format(orderId, order.items(), pr, taxPercent);
    }
}
