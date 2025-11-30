# 🎬 Video Recording Guide - Café POS Final Assessment

## 📋 Before You Start

### What You Need
- **Screen recording software** (choose one):
  - macOS: QuickTime Player (free, built-in)
  - Zoom (free tier allows recording)
  - OBS Studio (free, more advanced)
  - Loom (free tier)
- **Working microphone** (built-in is fine)
- **Your project open in VS Code or IntelliJ**
- **Terminal ready**
- **8 minutes maximum** (practice to stay under time!)

### Pre-Recording Checklist
```bash
# Navigate to project
cd /Users/darraghkennedy/22346945AND22349677

# Compile everything
mvn clean compile

# Test that all demos work (do this before recording!)
java -cp target/classes com.cafepos.demo.Week8Demo_Commands
java -cp target/classes com.cafepos.demo.Week8Demo_Adapter
java -cp target/classes com.cafepos.demo.Week9Demo_Menu
java -cp target/classes com.cafepos.demo.Week9Demo_State
java -cp target/classes com.cafepos.demo.Week10Demo_MVC
java -cp target/classes com.cafepos.demo.EventWiringDemo
```

---

## 🎯 Video Structure (8 Minutes Total)

### **Section 1: Introduction (30 seconds)** [0:00-0:30]

**What to say:**
> "Hello, I'm [Your Name]. This is my Café POS and Delivery system for the final assessment. Over the past weeks, I've implemented seven design patterns, a four-layer architecture, and event-driven components. Let me show you how they all work together."

**What to show:**
- Project open in IDE
- Quick glance at folder structure

**Tip:** Keep it brief, jump right into demos!

---

### **Section 2: Pattern Correctness & Integration (2.5 minutes)** [0:30-3:00]
**Worth 8 marks - This is your biggest section!**

#### 2A. Command Pattern (25 seconds)
**Terminal command:**
```bash
java -cp target/classes com.cafepos.demo.Week8Demo_Commands
```

**What to say while it runs:**
> "First, the Command pattern. This demo shows how PosRemote executes commands like AddItem and PayOrder. Notice the macro command combines multiple operations, and we can undo the last action. Commands decouple UI buttons from domain logic."

**What to show in IDE (while output displays):**
- Open `src/main/java/com/cafepos/command/`
- Briefly show `Command.java` interface
- Point to `AddItemCommand.java` - "encapsulates the action"
- Point to `PosRemote.java` - "invoker doesn't know what commands do"

#### 2B. Adapter Pattern (20 seconds)
**Terminal command:**
```bash
java -cp target/classes com.cafepos.demo.Week8Demo_Adapter
```

**What to say:**
> "The Adapter pattern integrates a legacy thermal printer without modifying our core code. The ThermalPrinterAdapter translates our Receipt interface to the vendor's LegacyThermalPrinter API."

**What to show in IDE:**
- Open `src/main/java/com/cafepos/printing/ThermalPrinterAdapter.java`
- Point to how it wraps `LegacyThermalPrinter`

#### 2C. Composite & Iterator (25 seconds)
**Terminal command:**
```bash
java -cp target/classes com.cafepos.demo.Week9Demo_Menu
```

**What to say:**
> "Composite and Iterator patterns handle hierarchical menus. Menu is the composite containing MenuItems. The CompositeIterator traverses the tree structure, here filtering only vegetarian options. This lets us treat individual items and groups uniformly."

**What to show in IDE:**
- Open `src/main/java/com/cafepos/menu/`
- Point to `MenuComponent.java` - "common interface"
- Show `Menu.java` (composite) and `MenuItem.java` (leaf)

#### 2D. State Pattern (25 seconds)
**Terminal command:**
```bash
java -cp target/classes com.cafepos.demo.Week9Demo_State
```

**What to say:**
> "The State pattern models order lifecycle. An order transitions from NEW to PREPARING to READY to DELIVERED. Each state has different allowed operations. Notice how it rejects invalid transitions like preparing before payment - the state objects encapsulate this logic, eliminating conditional chains."

**What to show in IDE:**
- Open `src/main/java/com/cafepos/state/`
- Show `State.java` interface
- Point to `NewState.java` and `PreparingState.java`

#### 2E. MVC Pattern (25 seconds)
**Terminal command:**
```bash
java -cp target/classes com.cafepos.demo.Week10Demo_MVC
```

**What to say:**
> "MVC separates concerns in the presentation layer. The OrderController handles user actions, delegating to CheckoutService. ConsoleView handles only output. The model is our domain objects. Notice how the controller doesn't format receipts - that's the application layer's job."

**What to show in IDE:**
- Open `src/main/java/com/cafepos/ui/OrderController.java`
- Point to `checkout()` method delegating to service
- Open `ConsoleView.java` - "just prints, no logic"

#### 2F. EventBus (25 seconds)
**Terminal command:**
```bash
java -cp target/classes com.cafepos.demo.EventWiringDemo
```

**What to say:**
> "Finally, the EventBus decouples components via publish-subscribe. When an order is created or paid, events flow through the bus. Multiple subscribers can react without tight coupling. This is a connector that enables future scalability."

**What to show in IDE:**
- Open `src/main/java/com/cafepos/app/events/EventBus.java`
- Show `on()` and `emit()` methods
- Open `OrderEvent.java` sealed interface

---

### **Section 3: Architectural Integrity (2 minutes)** [3:00-5:00]
**Worth 7 marks**

#### 3A. Four-Layer Overview (45 seconds)
**What to show:**
- In IDE, expand project structure to show packages:
  ```
  src/main/java/com/cafepos/
  ├── ui/          ← PRESENTATION
  ├── demo/        ← PRESENTATION
  ├── app/         ← APPLICATION
  ├── domain/      ← DOMAIN
  ├── pricing/     ← DOMAIN
  ├── payment/     ← DOMAIN
  ├── state/       ← DOMAIN
  └── infra/       ← INFRASTRUCTURE
  ```

**What to say:**
> "The architecture has four layers. Presentation handles UI and demos. Application contains services like CheckoutService that orchestrate use cases. Domain is the core - entities, policies, and business rules. Infrastructure provides adapters like InMemoryOrderRepository and the printer adapter. Dependencies point inward toward the domain."

#### 3B. Show Architecture Diagram (30 seconds)
**What to show:**
- Open `docs/Architecture-Diagram.md`
- Scroll to show the ASCII diagram
- Highlight the layer boxes

**What to say:**
> "Here's the visual representation. Notice how the domain layer has no dependencies on outer layers - it's pure business logic. Infrastructure and Application both depend on domain interfaces."

#### 3C. Connectors Demo (45 seconds)
**What to show in IDE:**

**Repository (Port & Adapter):**
- Open `src/main/java/com/cafepos/domain/OrderRepository.java`
- Say: "Domain defines this interface - a port"
- Open `src/main/java/com/cafepos/infra/InMemoryOrderRepository.java`
- Say: "Infrastructure provides the adapter implementation"

**EventBus (Connector):**
- Open `src/main/java/com/cafepos/app/events/EventBus.java`
- Say: "EventBus is our component connector - publishers and subscribers don't know about each other"

**Wiring (Composition Root):**
- Open `src/main/java/com/cafepos/infra/Wiring.java`
- Say: "All dependencies are wired here in one place, making the system testable"

---

### **Section 4: Code Quality & Testing (1.5 minutes)** [5:00-6:30]
**Worth 6 marks**

#### 4A. Code Organization (30 seconds)
**What to show:**
- Expand package structure in IDE
- Point out naming conventions

**What to say:**
> "Code is organized by architectural layer. Each package has a clear responsibility. Naming is consistent - repositories, services, strategies, states. Since midterm, I refactored from a God class to this layered structure, extracting CheckoutService, PricingService, and multiple strategy implementations."

#### 4B. Key Refactorings (20 seconds)
**What to show:**
- Open `README.md`, scroll to "Key Refactorings Applied" section

**What to say:**
> "Major refactorings included extracting DiscountPolicy and TaxPolicy strategies, removing global state, and introducing dependency injection. The old OrderManagerGod class violated SRP - now each class has one responsibility."

#### 4C. Tests (40 seconds)
**Terminal command:**
```bash
mvn test
```

**What to say before tests run:**
> " We have 45 tests covering order lifecycle, state transitions, menu filtering, command execution, payment strategies, and decorator combinations. Let's see the results..."

**When results appear:**
> "44 out of 45 tests pass. The one failure is a pre-existing issue in MenuTests unrelated to the Week 10 work. Test coverage includes edge cases like invalid state transitions, null handling, and pricing calculations."

**What to show in IDE:**
- Open `src/test/java/com/cafepos/`
- Point to `StateTests.java` - "tests order lifecycle"
- Point to `menu/MenuTests.java` - "tests menu traversal"
- Point to `observer/OrderObserverTests.java` - "tests event notifications"

---

### **Section 5: Trade-off Documentation (1.5 minutes)** [6:30-8:00]
**Worth 4 marks**

#### 5A. ADR Walkthrough (1 minute)
**What to show:**
- Open `docs/ADR-001-EventBus.md`

**What to say:**
> "Let me walk through our Architecture Decision Record for the EventBus. 

**Context:** As components grew, we needed decoupled communication. Multiple parts needed to react to domain events without tight coupling.

**Alternatives considered:** We evaluated four options - direct method calls, traditional Observer pattern, external message queues, and an EventBus.

**Decision:** We chose an in-process EventBus for publish-subscribe. 

**Consequences:** Positive - components are decoupled, easy to add new handlers, supports Open/Closed Principle. You can see this in the EventWiringDemo - UI subscribes to events without the domain knowing. Negative - slightly less type-safe than direct calls, flow is harder to trace. But for our current monolithic scale, the decoupling benefits outweigh these costs."

#### 5B. Layering vs Partitioning (30 seconds)
**What to show:**
- Open `README.md`, scroll to "Layering vs. Partitioning Trade-offs"

**What to say:**
> "We chose a layered monolith for simplicity at our current scale. But we identified natural seams for future partitioning - Payments could become a separate microservice for PCI compliance, Notifications could be an independent service, and the EventBus provides a clean boundary. We'd replace it with RabbitMQ or Kafka for distributed events."

---

### **Section 6: Conclusion (15 seconds)** [8:00 MAX]

**What to say:**
> "To summarize: seven design patterns working together, four clean architectural layers, comprehensive tests, and documented trade-offs. The system is maintainable, testable, and ready to evolve. Thank you."

**What to show:**
- Return to project root view in IDE

---

## 🎬 Recording Instructions

### Using QuickTime (macOS - Easiest)

1. **Open QuickTime Player**
2. **File → New Screen Recording**
3. **Click Options:**
   - Microphone: Built-in Microphone
   - Quality: High
4. **Click the red record button**
5. **Click anywhere on screen** or **drag to select area**
6. **Start your presentation!**
7. **When done:** Click stop button in menu bar
8. **File → Save** → Name it `CafePOS_Final_YourName.mov`

### Using Zoom

1. **Start a Zoom meeting** (can be alone)
2. **Share Screen** → Select your entire desktop or IDE window
3. **Click Record** → Record to this Computer
4. **Start presenting**
5. **Stop** when done
6. Zoom will convert to MP4 automatically

### Using OBS Studio (More Control)

1. **Download OBS** from obsproject.com
2. **Add Source** → Display Capture (for full screen) or Window Capture (for IDE only)
3. **Add Audio** → Built-in Microphone
4. **Settings → Output:** Recording format MP4, quality High
5. **Start Recording**
6. **Stop Recording** when done

---

## ✅ Final Checklist Before Recording

- [ ] All demos compile and run successfully
- [ ] IDE is set to comfortable font size (viewers can read code)
- [ ] Close unnecessary applications
- [ ] Turn off notifications (Do Not Disturb mode)
- [ ] Have terminal and IDE visible (split screen or switch between them)
- [ ] README and ADR files are ready to show
- [ ] Practice run-through (should be under 8 minutes)
- [ ] Check microphone audio level

---

## 💡 Pro Tips

1. **Practice first!** Do a dry run and time yourself
2. **Speak clearly** but at a moderate pace - rushing makes you hard to understand
3. **Show, don't just tell** - run the demos, open the files
4. **Use your cursor** - point to important code sections
5. **Don't worry about perfection** - small mistakes are fine, just keep going
6. **Stay calm** - you know your project, just present what you built
7. **Check the time** - glance at a clock, aim for 7:30 to leave buffer
8. **End strong** - confident summary

---

## 🚨 Common Mistakes to Avoid

- ❌ Spending too long on setup/introduction
- ❌ Reading code line by line (just highlight key parts)
- ❌ Going over 8 minutes (will be cut off!)
- ❌ Showing errors/failures without explanation
- ❌ Mumbling or speaking too fast
- ❌ Forgetting to show tests running
- ❌ Not connecting ADR back to actual code

---

## 📤 Submission

After recording:

1. **Review your video** - watch it once to check:
   - Audio is clear
   - Screen is visible
   - Under 8 minutes
   - All 4 rubric sections covered

2. **File format:** MP4 or MOV preferred

3. **File size:** Keep under 500MB if possible (reduce quality if needed)

4. **Upload** to your submission platform

---

## 🎯 Rubric Checklist (Self-Grade Before Submitting)

### Pattern Correctness & Integration (8 marks)
- [ ] Showed Command pattern demo
- [ ] Showed Adapter pattern demo  
- [ ] Showed Composite & Iterator demo
- [ ] Showed State pattern demo
- [ ] Showed MVC pattern demo
- [ ] Showed EventBus demo
- [ ] Explained how each pattern works
- [ ] Pointed to relevant code files

### Architectural Integrity (7 marks)
- [ ] Showed 4-layer structure in IDE
- [ ] Explained layer responsibilities
- [ ] Showed architecture diagram
- [ ] Demonstrated connectors (EventBus, Repository)
- [ ] Explained dependency direction (inward)

### Code Quality & Testing (6 marks)
- [ ] Showed package organization
- [ ] Mentioned refactoring from midterm
- [ ] Ran tests with `mvn test`
- [ ] Explained what tests cover
- [ ] Showed test files in IDE

### Trade-off Documentation (4 marks)
- [ ] Showed ADR-001-EventBus.md
- [ ] Explained context/problem
- [ ] Mentioned alternatives
- [ ] Explained decision
- [ ] Discussed consequences (pros/cons)
- [ ] Connected to actual code

---

**You've got this! Your project is complete and well-structured. Just present it confidently!** 🚀

Good luck! 🍀
