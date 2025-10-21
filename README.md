## Reflection & Evidence of Effort
- Removed smells in `com.cafepos.smells.OrderManagerGod`: God Class/Long Method, Primitive Obsession (string codes, magic numbers), Duplicated Logic (BigDecimal math), Feature Envy/Shotgun Surgery (inline tax/discount), Global State (static fields).
- Applied refactorings: Extract Class (DiscountPolicy, TaxPolicy, ReceiptPrinter), Extract Method, Introduce Strategy, Replace Conditional with Polymorphism, Dependency Injection, Remove Global State.
- New design: `checkout.OrderManager` orchestrates via injected interfaces; `pricing.*` encapsulates rules; `ReceiptPrinter` handles formatting; `OrderManagerFactory` wires dependencies.
- SOLID: SRP (each class has one reason to change), OCP (add policies without modifying callers), LSP (policies interchangeable), ISP (small focused interfaces), DIP (OrderManager depends on abstractions).
- To add a new discount: implement `DiscountPolicy` (e.g., `HappyHourDiscount`) and register/select it in a factory/composition root—no changes to `OrderManager` or existing policies required.
