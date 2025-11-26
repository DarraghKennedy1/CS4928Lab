# ADR 001: EventBus for Component Decoupling

**Date:** 2025-11-26  
**Status:** Accepted

## Context

As our Café POS system grew to include multiple components (UI controllers, application services, domain model, and infrastructure adapters), we needed a way for these components to communicate without creating tight coupling. Specifically, when domain events occur (order created, order paid, order ready), multiple parts of the system need to react (update UI, send notifications, log events).

The challenge was: how do we allow components to react to events without hardwiring dependencies between them?

## Decision

We will use an **in-process EventBus** as the primary connector for cross-component communication. Components publish domain events (OrderCreated, OrderPaid) to the bus, and other components subscribe to events they care about.

## Alternatives Considered

### 1. Direct Method Calls
- **Pros:** Simple, explicit, type-safe at compile time
- **Cons:** Creates tight coupling; adding new subscribers requires modifying publishers; violates Open/Closed Principle; difficult to test in isolation

### 2. Observer Pattern (Traditional)
- **Pros:** Standard pattern, well-understood
- **Cons:** Each subject needs its own observer list; subscribers still directly coupled to specific subjects; boilerplate code for each event type

### 3. Message Queue (External)
- **Pros:** True decoupling, supports distributed systems, persistence
- **Cons:** Massive overhead for a monolithic console app; requires external infrastructure; complexity not justified for current scale

### 4. EventBus (Chosen Solution)
- **Pros:** Decouples publishers from subscribers; easy to add new event handlers without modifying existing code; supports multiple subscribers per event; simple in-process implementation; testable
- **Cons:** Less type-safe than direct calls (runtime resolution); can make flow harder to trace; potential for orphaned handlers

## Consequences

### Positive
- **Decoupling:** UI layer can react to domain events without domain knowing about UI
- **Extensibility:** New event handlers (logging, analytics, notifications) can be added without modifying existing components
- **Open/Closed Principle:** System is open for extension (new handlers) but closed for modification (existing publishers)
- **Testability:** Components can be tested in isolation; event handlers can be mocked

### Negative
- **Debugging:** Event flow less obvious than direct calls; requires logging or debugging tools to trace
- **Runtime Errors:** Type mismatches caught at runtime, not compile time
- **Documentation:** Need to document which components emit/consume which events

### Neutral
- **Current Implementation:** In-memory bus suitable for current monolithic architecture
- **Future Evolution:** Could be replaced with external message broker if system becomes distributed
- **Seams for Partitioning:** EventBus creates natural boundaries for future microservices (Notifications, Payments could become separate services listening to events)

## Implementation Notes

See `com.cafepos.app.events.EventBus` and `EventWiringDemo` for reference implementation.

The EventBus uses a type-safe pub/sub model:
```java
bus.on(OrderCreated.class, e -> handleOrderCreated(e));
bus.emit(new OrderCreated(orderId));
```

This pattern is demonstrated in `EventWiringDemo.java`.
