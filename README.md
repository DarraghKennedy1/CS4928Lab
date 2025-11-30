# Café POS & Delivery System

## Project Overview
This project implements a comprehensive Point of Sale system for a café, demonstrating advanced design patterns, clean architecture principles, and SOLID design. The system handles order management, product customization, pricing strategies, payment processing, and order lifecycle management through a layered architectural approach.

## Architecture

### Four-Layer Architecture
The system is organized into four distinct layers following the principle of dependency inversion:

1. **Presentation Layer** (`ui/`, `demo/`)
   - User interface components and controllers
   - Console views for output
   - Demo applications showcasing different patterns
   - **Dependencies:** Application layer

2. **Application Layer** (`app/`)
   - Use case orchestration (CheckoutService)
   - Receipt formatting
   - Event definitions
   - **Dependencies:** Domain layer

3. **Domain Layer** (`domain/`, `pricing/`, `payment/`, `state/`, etc.)
   - Core business entities (Order, LineItem, Product)
   - Value objects (Money)
   - Business policies (DiscountPolicy, TaxPolicy, PaymentStrategy)
   - Repository interfaces
   - State machines for order lifecycle
   - **Dependencies:** None (pure business logic)

4. **Infrastructure Layer** (`infra/`)
   - Persistence adapters (InMemoryOrderRepository)
   - External system integrations (LegacyThermalPrinter adapter)
   - Dependency wiring (Wiring composition root)
   - **Dependencies:** Domain and Application layers

### Layering vs. Partitioning Trade-offs

**Why Layered Monolith (Current Choice)?**
We chose a layered monolithic architecture for simplicity and rapid development. At our current scale, a single deployable unit reduces operational complexity, enables easier debugging, and avoids the overhead of distributed systems. All layers run in-process, sharing memory and avoiding network latency. This keeps our build, test, and deployment pipeline straightforward while we validate the business model.

**Natural Seams for Future Partitioning:**
- **Payments Service:** The PaymentStrategy implementations and payment processing could become a separate microservice, communicating via REST API or message queue for PCI compliance
- **Notifications Service:** The Observer pattern (KitchenDisplay, CustomerNotifier, DeliveryDesk) could evolve into an independent notification service subscribing to OrderEvents
- **Catalog Service:** Product factory and menu management could become a separate service for managing inventory

**Connectors for Partitioning:**
If we partition in the future, the EventBus provides a natural seam. We would replace the in-process EventBus with a message broker (RabbitMQ, Kafka) using the same event structures (OrderCreated, OrderPaid). Services would expose REST APIs for synchronous operations and publish/subscribe to events for async workflows. Repository interfaces would adapt to network-based data access.

## Design Patterns Implemented

### Creational Patterns
- **Factory Pattern** (`ProductFactory`): Creates products and decorators from recipe strings
- **Composition Root** (`Wiring`): Centralizes dependency injection and object creation

### Structural Patterns
- **Decorator Pattern** (`ExtraShot`, `OatMilk`, `SoyMilk`, etc.): Dynamically adds features to products
- **Adapter Pattern** (`ThermalPrinterAdapter`): Integrates legacy printer without modifying core code
- **Composite Pattern** (`Menu`, `MenuItem`, `MenuComponent`): Hierarchical menu structures

### Behavioral Patterns
- **Strategy Pattern** 
  - Payment strategies (`CashPayment`, `CardPayment`, `MobilePayment`)
  - Pricing policies (`DiscountPolicy`, `TaxPolicy`)
- **Observer Pattern** (`OrderObserver`, `KitchenDisplay`, `CustomerNotifier`): React to order events
- **Command Pattern** (`AddItemCommand`, `PayOrderCommand`, `MacroCommand`): Encapsulate actions with undo support
- **State Pattern** (`NewState`, `PreparingState`, `ReadyState`, `DeliveredState`, `CancelledState`): Manage order lifecycle
- **Iterator Pattern** (`CompositeIterator`): Traverse menu hierarchies

### Architectural Patterns
- **Model-View-Controller (MVC)**: Separates UI concerns in presentation layer
- **Repository Pattern** (`OrderRepository`): Abstracts data persistence
- **Event Bus**: Decouples components via publish/subscribe

## Running the Demos

### Prerequisites
```bash
# Compile the project
mvn clean compile
```

### Week 8: Command & Adapter
```bash
# Command Pattern Demo
java -cp target/classes com.cafepos.demo.Week8Demo_Commands

# Adapter Pattern Demo  
java -cp target/classes com.cafepos.demo.Week8Demo_Adapter
```

### Week 9: Composite, Iterator & State
```bash
# Menu Composite/Iterator Demo
java -cp target/classes com.cafepos.demo.Week9Demo_Menu

# State Pattern Demo
java -cp target/classes com.cafepos.demo.Week9Demo_State
```

### Week 10: Layered Architecture & MVC
```bash
# MVC Pattern Demo
java -cp target/classes com.cafepos.demo.Week10Demo_MVC

# Event Bus Demo
java -cp target/classes com.cafepos.demo.EventWiringDemo
```

### Run All Tests
```bash
mvn test
```

## SOLID Principles

- **Single Responsibility (SRP)**: Each class has one reason to change
  - `Order` manages items, `PricingService` handles pricing, `ReceiptFormatter` formats output
  
- **Open/Closed (OCP)**: Open for extension, closed for modification
  - Add new decorators, payment strategies, or event handlers without changing existing code
  
- **Liskov Substitution (LSP)**: Policies are interchangeable
  - Any `DiscountPolicy` or `PaymentStrategy` can substitute for another
  
- **Interface Segregation (ISP)**: Small, focused interfaces
  - `OrderRepository`, `DiscountPolicy`, `TaxPolicy` have minimal, specific contracts
  
- **Dependency Inversion (DIP)**: Depend on abstractions, not concretions
  - `OrderController` depends on `OrderRepository` interface, not implementation

## Key Refactorings Applied

## Reflection & Evidence of Effort
- Removed smells in `com.cafepos.smells.OrderManagerGod`: God Class/Long Method, Primitive Obsession (string codes, magic numbers), Duplicated Logic (BigDecimal math), Feature Envy/Shotgun Surgery (inline tax/discount), Global State (static fields).
- Applied refactorings: Extract Class (DiscountPolicy, TaxPolicy, ReceiptPrinter), Extract Method, Introduce Strategy, Replace Conditional with Polymorphism, Dependency Injection, Remove Global State.
- New design: `checkout.OrderManager` orchestrates via injected interfaces; `pricing.*` encapsulates rules; `ReceiptPrinter` handles formatting; `OrderManagerFactory` wires dependencies.
- SOLID: SRP (each class has one reason to change), OCP (add policies without modifying callers), LSP (policies interchangeable), ISP (small focused interfaces), DIP (OrderManager depends on abstractions).
- To add a new discount: implement `DiscountPolicy` (e.g., `HappyHourDiscount`) and register/select it in a factory/composition root—no changes to `OrderManager` or existing policies required.

## Video Link
https://ulcampus-my.sharepoint.com/:v:/r/personal/22349677_studentmail_ul_ie/Documents/Full%20Abdul%20Project%20(Submit).mp4?csf=1&web=1&e=svAkFv&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D
