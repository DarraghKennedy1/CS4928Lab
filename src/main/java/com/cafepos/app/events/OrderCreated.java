package com.cafepos.app.events;

/**
 * Event emitted when an order is created.
 */
public record OrderCreated(long orderId) implements OrderEvent {}
