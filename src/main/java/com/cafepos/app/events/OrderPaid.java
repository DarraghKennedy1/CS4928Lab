package com.cafepos.app.events;

/**
 * Event emitted when an order is paid.
 */
public record OrderPaid(long orderId) implements OrderEvent {}
