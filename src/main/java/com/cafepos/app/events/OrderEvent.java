package com.cafepos.app.events;

/**
 * Sealed interface for order-related events.
 * Part of the event-driven architecture.
 */
public sealed interface OrderEvent permits OrderCreated, OrderPaid {}
