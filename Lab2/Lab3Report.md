# Lab#3 - Behavioral Design Patterns - Pascari Vasile

## Objectives
Extend the coffee shop ordering system by implementing **3 Behavioral Design Patterns** to manage order workflows, enable flexible payment processing, and provide real-time notifications to customers and staff.

## Task Requirements

✅ **Implement at least 3 behavioral design patterns** - Patterns that characterize ways in which classes or objects interact and distribute responsibility  
✅ **Integrate patterns with existing system** - Work seamlessly with creational and structural patterns from Labs 1-2  
✅ **Single unified client** - All functionality accessible through `CoffeeApp.java`  

---

## Implemented Behavioral Design Patterns

### 1. Observer Pattern

**Purpose:** Define a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically. Used for real-time order status notifications.

**Location:** `lab2/observer/` package

**Implementation Structure:**

```java
// Observer Interface
public interface OrderObserver {
    void onOrderPlaced(OrderComponent order, String orderId);
    void onOrderPreparing(String orderId);
    void onOrderReady(String orderId);
    void onOrderCancelled(String orderId);
}

// Subject Interface
public interface OrderSubject {
    void addObserver(OrderObserver observer);
    void removeObserver(OrderObserver observer);
    void notifyOrderPlaced(String orderId);
    void notifyOrderPreparing(String orderId);
    void notifyOrderReady(String orderId);
    void notifyOrderCancelled(String orderId);
}

// Concrete Observer - Customer Notifications
public class CustomerNotificationObserver implements OrderObserver {
    private final String customerName;

    @Override
    public void onOrderPlaced(OrderComponent order, String orderId) {
        System.out.println("[SMS to " + customerName + "] Your order #" + orderId + 
            " has been placed! Total: $" + String.format("%.2f", order.getTotalCost()));
    }

    @Override
    public void onOrderReady(String orderId) {
        System.out.println("[SMS to " + customerName + "] Your order #" + orderId + 
            " is ready for pickup! Enjoy your coffee!");
    }
    // ... other notification methods
}

// Concrete Observer - Kitchen Display
public class KitchenDisplayObserver implements OrderObserver {
    @Override
    public void onOrderPlaced(OrderComponent order, String orderId) {
        System.out.println("[KITCHEN DISPLAY] NEW ORDER #" + orderId);
        System.out.println("[KITCHEN DISPLAY] Items: " + order.getDescription());
    }

    @Override
    public void onOrderPreparing(String orderId) {
        System.out.println("[KITCHEN DISPLAY] Order #" + orderId + " -> IN PROGRESS");
    }
    // ... other display updates
}

// Concrete Observer - Loyalty Points Tracker
public class LoyaltyPointsObserver implements OrderObserver {
    private int totalPoints;
    private static final int POINTS_PER_DOLLAR = 10;

    @Override
    public void onOrderPlaced(OrderComponent order, String orderId) {
        int potentialPoints = (int) (order.getTotalCost() * POINTS_PER_DOLLAR);
        System.out.println("[LOYALTY] You will earn " + potentialPoints + 
            " points for order #" + orderId);
    }
    // ... point tracking methods
}
```

**Benefits:**
- Loose coupling between order management and notification systems
- Easy to add new notification channels (email, push notifications, etc.)
- Real-time updates to multiple stakeholders simultaneously
- Observers can be added/removed dynamically at runtime

**Usage in System:**
```java
// In CoffeeShopFacade - Observer setup
OrderManager orderManager = new OrderManager();
orderManager.addObserver(new KitchenDisplayObserver());
orderManager.addObserver(new LoyaltyPointsObserver());
orderManager.addObserver(new CustomerNotificationObserver("John"));

// When order status changes, all observers are notified automatically
orderManager.updateOrderStatus(orderId, OrderStatus.READY);
// Output:
// [KITCHEN DISPLAY] Order #1001 -> COMPLETED ✓
// [SMS to John] Your order #1001 is ready for pickup!
// [LOYALTY] You earned 50 points! Total: 50 points
```

---

### 2. Strategy Pattern

**Purpose:** Define a family of algorithms, encapsulate each one, and make them interchangeable. Strategy lets the algorithm vary independently from clients that use it. Used for flexible payment processing.

**Location:** `lab2/strategy/` package

**Implementation Structure:**

```java
// Strategy Interface
public interface PaymentStrategy {
    boolean pay(double amount);
    String getPaymentMethod();
}

// Concrete Strategy - Cash Payment
public class CashPaymentStrategy implements PaymentStrategy {
    private double cashReceived;

    @Override
    public boolean pay(double amount) {
        if (cashReceived >= amount) {
            double change = cashReceived - amount;
            System.out.println("[PAYMENT] Amount due: $" + String.format("%.2f", amount));
            System.out.println("[PAYMENT] Cash received: $" + String.format("%.2f", cashReceived));
            if (change > 0) {
                System.out.println("[PAYMENT] Change: $" + String.format("%.2f", change));
            }
            return true;
        }
        return false;
    }

    @Override
    public String getPaymentMethod() {
        return "Cash";
    }
}

// Concrete Strategy - Credit Card Payment
public class CreditCardPaymentStrategy implements PaymentStrategy {
    private final String cardNumber;
    private final String cardHolderName;
    private final String cvv;
    private final String expiryDate;

    @Override
    public boolean pay(double amount) {
        if (validateCard()) {
            System.out.println("[PAYMENT] Card: **** **** **** " + 
                cardNumber.substring(cardNumber.length() - 4));
            System.out.println("[PAYMENT] Amount: $" + String.format("%.2f", amount));
            System.out.println("[PAYMENT] Credit card payment successful!");
            return true;
        }
        return false;
    }
}

// Concrete Strategy - Mobile Payment
public class MobilePaymentStrategy implements PaymentStrategy {
    private final String phoneNumber;
    private final String paymentApp; // "Apple Pay", "Google Pay"

    @Override
    public boolean pay(double amount) {
        System.out.println("[PAYMENT] Initiating " + paymentApp + " payment...");
        System.out.println("[PAYMENT] " + paymentApp + " payment successful! ✓");
        return true;
    }
}

// Concrete Strategy - Loyalty Points Payment
public class LoyaltyPointsPaymentStrategy implements PaymentStrategy {
    private int availablePoints;
    private static final int POINTS_PER_DOLLAR = 100;

    @Override
    public boolean pay(double amount) {
        int requiredPoints = (int) (amount * POINTS_PER_DOLLAR);
        if (availablePoints >= requiredPoints) {
            availablePoints -= requiredPoints;
            System.out.println("[PAYMENT] Points redeemed successfully!");
            return true;
        }
        return false;
    }
}

// Context - Payment Processor
public class PaymentProcessor {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public boolean processPayment(double amount) {
        System.out.println("\n--- Processing Payment ---");
        System.out.println("Payment Method: " + paymentStrategy.getPaymentMethod());
        boolean result = paymentStrategy.pay(amount);
        System.out.println("--------------------------\n");
        return result;
    }
}
```

**Benefits:**
- Payment methods can be changed at runtime
- Easy to add new payment types without modifying existing code
- Each payment strategy encapsulates its own validation and processing logic
- Clean separation of payment algorithms from the ordering system

**Usage in System:**
```java
PaymentProcessor processor = new PaymentProcessor();

// Customer chooses cash
processor.setPaymentStrategy(new CashPaymentStrategy(20.00));
processor.processPayment(15.50);
// Output: Change: $4.50

// Customer switches to mobile payment
processor.setPaymentStrategy(new MobilePaymentStrategy("555-1234", "Apple Pay"));
processor.processPayment(15.50);
// Output: Apple Pay payment successful! ✓
```

---

### 3. Command Pattern

**Purpose:** Encapsulate a request as an object, thereby letting you parameterize clients with different requests, queue or log requests, and support undoable operations. Used for order management with undo/redo.

**Location:** `lab2/command/` package

**Implementation Structure:**

```java
// Command Interface
public interface OrderCommand {
    void execute();
    void undo();
    String getDescription();
}

// Concrete Command - Place Order
public class PlaceOrderCommand implements OrderCommand {
    private final OrderManager orderManager;
    private final OrderComponent order;
    private String orderId;

    @Override
    public void execute() {
        orderId = orderManager.addOrder(order);
        System.out.println("[COMMAND] Order placed with ID: " + orderId);
    }

    @Override
    public void undo() {
        if (orderId != null) {
            orderManager.removeOrder(orderId);
            System.out.println("[COMMAND] Order " + orderId + " has been undone (removed).");
        }
    }

    @Override
    public String getDescription() {
        return "Place order: " + order.getDescription();
    }
}

// Concrete Command - Cancel Order
public class CancelOrderCommand implements OrderCommand {
    private final OrderManager orderManager;
    private final String orderId;
    private OrderManager.OrderEntry cancelledOrder;

    @Override
    public void execute() {
        cancelledOrder = orderManager.getOrderEntry(orderId);
        if (cancelledOrder != null) {
            orderManager.cancelOrder(orderId);
            System.out.println("[COMMAND] Order " + orderId + " has been cancelled.");
        }
    }

    @Override
    public void undo() {
        if (cancelledOrder != null) {
            orderManager.restoreOrder(orderId, cancelledOrder);
            System.out.println("[COMMAND] Order " + orderId + " has been restored.");
        }
    }
}

// Concrete Command - Update Order Status
public class UpdateOrderStatusCommand implements OrderCommand {
    private final OrderManager orderManager;
    private final String orderId;
    private final OrderManager.OrderStatus newStatus;
    private OrderManager.OrderStatus previousStatus;

    @Override
    public void execute() {
        previousStatus = orderManager.getOrderStatus(orderId);
        orderManager.updateOrderStatus(orderId, newStatus);
        System.out.println("[COMMAND] Order " + orderId + " status updated: " + 
            previousStatus + " -> " + newStatus);
    }

    @Override
    public void undo() {
        orderManager.updateOrderStatus(orderId, previousStatus);
        System.out.println("[COMMAND] Order " + orderId + " status reverted: " + 
            newStatus + " -> " + previousStatus);
    }
}

// Invoker - Command History Manager
public class OrderCommandInvoker {
    private final Stack<OrderCommand> commandHistory;
    private final Stack<OrderCommand> undoneCommands;

    public void executeCommand(OrderCommand command) {
        command.execute();
        commandHistory.push(command);
        undoneCommands.clear();
    }

    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            OrderCommand command = commandHistory.pop();
            command.undo();
            undoneCommands.push(command);
        }
    }

    public void redoLastCommand() {
        if (!undoneCommands.isEmpty()) {
            OrderCommand command = undoneCommands.pop();
            command.execute();
            commandHistory.push(command);
        }
    }
}

// Receiver - Order Manager (also implements Observer Subject)
public class OrderManager implements OrderSubject {
    private final Map<String, OrderEntry> orders;
    private final List<OrderObserver> observers;

    public enum OrderStatus {
        PLACED, PREPARING, READY, COMPLETED, CANCELLED
    }

    public String addOrder(OrderComponent order) {
        String orderId = generateOrderId();
        orders.put(orderId, new OrderEntry(order, OrderStatus.PLACED));
        notifyOrderPlaced(orderId);
        return orderId;
    }

    public void updateOrderStatus(String orderId, OrderStatus status) {
        OrderEntry entry = orders.get(orderId);
        entry.setStatus(status);
        // Notify observers based on status
        switch (status) {
            case PREPARING: notifyOrderPreparing(orderId); break;
            case READY: notifyOrderReady(orderId); break;
            case CANCELLED: notifyOrderCancelled(orderId); break;
        }
    }
}
```

**Benefits:**
- Complete undo/redo functionality for all order operations
- Command history for audit trail and debugging
- Decouples the invoker from the receiver
- Easy to add new commands without modifying existing code
- Commands can be queued, logged, or transmitted over network

**Usage in System:**
```java
OrderCommandInvoker invoker = new OrderCommandInvoker();

// Place an order
OrderCommand placeCmd = new PlaceOrderCommand(orderManager, latteOrder);
invoker.executeCommand(placeCmd);
// Output: [COMMAND] Order placed with ID: 1001

// Update status
OrderCommand updateCmd = new UpdateOrderStatusCommand(orderManager, "1001", PREPARING);
invoker.executeCommand(updateCmd);
// Output: [COMMAND] Order 1001 status updated: Order Placed -> Preparing

// Oops! Undo the status update
invoker.undoLastCommand();
// Output: [COMMAND] Order 1001 status reverted: Preparing -> Order Placed

// Redo it
invoker.redoLastCommand();
// Output: [COMMAND] Order 1001 status updated: Order Placed -> Preparing
```

---

## Integration with Facade Pattern

The `CoffeeShopFacade` has been extended to integrate all behavioral patterns:

```java
public class CoffeeShopFacade {
    private final OrderService orderService;
    
    // Behavioral Patterns components
    private final OrderManager orderManager;           // Observer Pattern (Subject)
    private final OrderCommandInvoker commandInvoker;  // Command Pattern (Invoker)
    private final PaymentProcessor paymentProcessor;   // Strategy Pattern (Context)
    
    // Observers
    private CustomerNotificationObserver customerObserver;
    private final KitchenDisplayObserver kitchenObserver;
    private final LoyaltyPointsObserver loyaltyObserver;

    public CoffeeShopFacade() {
        // Initialize all components
        this.orderManager = new OrderManager();
        this.commandInvoker = new OrderCommandInvoker();
        this.paymentProcessor = new PaymentProcessor();
        
        // Register default observers
        this.kitchenObserver = new KitchenDisplayObserver();
        this.loyaltyObserver = new LoyaltyPointsObserver();
        orderManager.addObserver(kitchenObserver);
        orderManager.addObserver(loyaltyObserver);
    }
    
    // Observer Pattern: Set customer for notifications
    public void setCustomerName(String customerName) {
        if (customerObserver != null) {
            orderManager.removeObserver(customerObserver);
        }
        customerObserver = new CustomerNotificationObserver(customerName);
        orderManager.addObserver(customerObserver);
    }
    
    // Command Pattern: Place order with undo support
    public String placeOrderWithCommand(OrderComponent order) {
        PlaceOrderCommand command = new PlaceOrderCommand(orderManager, order);
        commandInvoker.executeCommand(command);
        return command.getOrderId();
    }
    
    // Command Pattern: Undo/Redo operations
    public void undoLastCommand() { commandInvoker.undoLastCommand(); }
    public void redoLastCommand() { commandInvoker.redoLastCommand(); }
    
    // Strategy Pattern: Payment methods
    public void setPaymentCash(double cashAmount) {
        paymentProcessor.setPaymentStrategy(new CashPaymentStrategy(cashAmount));
    }
    
    public void setPaymentCreditCard(String cardNumber, String holderName, 
                                      String cvv, String expiry) {
        paymentProcessor.setPaymentStrategy(
            new CreditCardPaymentStrategy(cardNumber, holderName, cvv, expiry));
    }
    
    public void setPaymentMobile(String phoneNumber, String paymentApp) {
        paymentProcessor.setPaymentStrategy(
            new MobilePaymentStrategy(phoneNumber, paymentApp));
    }
    
    public boolean processPayment(double amount) {
        return paymentProcessor.processPayment(amount);
    }
}
```

---

## Updated Client Application

The `CoffeeApp.java` has been extended with new menu options:

```java
public final class CoffeeApp {
    private static final CoffeeShopFacade facade = new CoffeeShopFacade();
    private static String lastOrderId = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Observer Pattern: Set customer name for notifications
        System.out.print("Welcome! Please enter your name: ");
        String customerName = scanner.nextLine().trim();
        facade.setCustomerName(customerName);
        
        while (running) {
            printMainMenu();
            switch (choice) {
                case 1: orderSingleCoffee(scanner); break;
                case 2: orderDecoratedCoffee(scanner); break;
                case 3: orderPopularCoffee(scanner); break;
                case 4: orderCombo(scanner); break;
                case 5: manageOrders(scanner); break;      // NEW: Command Pattern
                case 6: processPayment(scanner); break;    // NEW: Strategy Pattern
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n=== Coffee Shop Menu ===");
        System.out.println("1. Order Single Coffee");
        System.out.println("2. Order Decorated Coffee (with toppings)");
        System.out.println("3. Order Popular Coffee (preset decorations)");
        System.out.println("4. Order Combo Deal");
        System.out.println("5. Manage Orders (Command Pattern)");
        System.out.println("6. Process Payment (Strategy Pattern)");
        System.out.println("0. Exit");
    }
    
    // Command Pattern menu
    private static void manageOrders(Scanner scanner) {
        System.out.println("\n=== Order Management ===");
        System.out.println("1. View All Orders");
        System.out.println("2. Update Order Status");
        System.out.println("3. Cancel Order");
        System.out.println("4. Undo Last Action");
        System.out.println("5. Redo Last Action");
        System.out.println("6. View Command History");
        // ... handle choices
    }
    
    // Strategy Pattern menu
    private static void processPayment(Scanner scanner) {
        System.out.println("\n=== Payment ===");
        System.out.println("1. Cash");
        System.out.println("2. Credit Card");
        System.out.println("3. Mobile Payment (Apple Pay / Google Pay)");
        System.out.println("4. Loyalty Points");
        // ... handle payment selection and processing
    }
}
```

---

## Example Interactions

### Example 1: Complete Order Flow with All Patterns

```
Welcome! Please enter your name: Vasea
Hello, Vasea! Let's get you some coffee.

=== Coffee Shop Menu ===
Select an option: 1

Select Coffee Type:
1. Espresso
2. Latte
3. Cappuccino
Choice: 2

Preferred size (S/M/L): L
Milk choice: Oat
Takeaway? (y/N): n

========== ORDER SUMMARY ==========
  - Latte ($4.55)
-----------------------------------
Total Items: 1
Total Cost: $4.55
===================================

--- Placing Order (Command Pattern) ---
[COMMAND] Order placed with ID: 1001
[KITCHEN DISPLAY] NEW ORDER #1001
[KITCHEN DISPLAY] Items: Latte
[SMS to Vasea] Your order #1001 has been placed! Total: $4.55
[LOYALTY] You will earn 45 points for order #1001
```

**Patterns Used:**
- **Command**: `PlaceOrderCommand` encapsulates the order placement
- **Observer**: Kitchen display, SMS, and loyalty system all notified

---

### Example 2: Order Management with Undo/Redo

```
=== Order Management (Command Pattern) ===
Select: 2 (Update Order Status)

Enter Order ID: 1001
Select new status:
1. Preparing
2. Ready
Choice: 1

[KITCHEN DISPLAY] Order #1001 -> IN PROGRESS
[SMS to Vasea] Your order #1001 is now being prepared by our barista!
[COMMAND] Order 1001 status updated: Order Placed -> Preparing

Select: 4 (Undo Last Action)

--- Undo (Command Pattern) ---
[KITCHEN DISPLAY] Order #1001 -> IN PROGRESS
[SMS to Vasea] Your order #1001 is now being prepared by our barista!
[COMMAND] Order 1001 status reverted: Preparing -> Order Placed

Select: 5 (Redo Last Action)

--- Redo (Command Pattern) ---
[KITCHEN DISPLAY] Order #1001 -> IN PROGRESS
[COMMAND] Order 1001 status updated: Order Placed -> Preparing
```

**Patterns Used:**
- **Command**: Status update encapsulated with undo capability
- **Observer**: Notifications triggered on each status change

---

### Example 3: Payment with Different Strategies

```
=== Payment (Strategy Pattern) ===
Select payment method:
1. Cash
2. Credit Card
3. Mobile Payment
4. Loyalty Points
Choice: 1

Enter amount to pay: $4.55
Enter cash amount: $10.00

--- Processing Payment ---
Payment Method: Cash
[PAYMENT] Processing cash payment...
[PAYMENT] Amount due: $4.55
[PAYMENT] Cash received: $10.00
[PAYMENT] Change: $5.45
[PAYMENT] Cash payment successful!
--------------------------

Payment successful! Thank you for your order.
[KITCHEN DISPLAY] Order #1001 -> IN PROGRESS
[SMS to Vasea] Your order #1001 is now being prepared!
```

```
Choice: 3 (Mobile Payment)
Payment app: 1 (Apple Pay)

--- Processing Payment ---
Payment Method: Apple Pay
[PAYMENT] Initiating Apple Pay payment...
[PAYMENT] Sending payment request to ***-***-1234...
[PAYMENT] Amount: $4.55
[PAYMENT] Apple Pay payment successful! ✓
--------------------------
```

**Patterns Used:**
- **Strategy**: Payment method selected and processed at runtime
- **Observer**: Order status updated after successful payment

---

## How Requirements Are Met

### ✅ Requirement 1: At Least 3 Behavioral Design Patterns

| Pattern | Classes | Purpose |
|---------|---------|---------|
| **Observer** | 5 classes | Real-time notifications to customers, kitchen, loyalty system |
| **Strategy** | 6 classes | Flexible payment processing (Cash, Card, Mobile, Points) |
| **Command** | 6 classes | Order operations with undo/redo support |

### ✅ Requirement 2: Patterns Characterize Interactions

- **Observer**: Defines how order changes propagate to multiple listeners
- **Strategy**: Defines interchangeable payment algorithms
- **Command**: Encapsulates operations as objects for execution control

### ✅ Requirement 3: Integration with Existing System

- All patterns integrated through `CoffeeShopFacade`
- Works seamlessly with existing Creational patterns (Factory, Builder, etc.)
- Works seamlessly with existing Structural patterns (Decorator, Composite, Facade)
- Single client (`CoffeeApp.java`) accesses all functionality

---

## Project Structure

```
Lab2/
├── src/
│   └── lab2/
│       ├── client/
│       │   └── CoffeeApp.java              (Updated unified client)
│       ├── facade/
│       │   └── CoffeeShopFacade.java       (Updated with behavioral patterns)
│       │
│       ├── observer/                        (NEW - Observer Pattern)
│       │   ├── OrderObserver.java          (Observer interface)
│       │   ├── OrderSubject.java           (Subject interface)
│       │   ├── CustomerNotificationObserver.java
│       │   ├── KitchenDisplayObserver.java
│       │   └── LoyaltyPointsObserver.java
│       │
│       ├── strategy/                        (NEW - Strategy Pattern)
│       │   ├── PaymentStrategy.java        (Strategy interface)
│       │   ├── PaymentProcessor.java       (Context)
│       │   ├── CashPaymentStrategy.java
│       │   ├── CreditCardPaymentStrategy.java
│       │   ├── MobilePaymentStrategy.java
│       │   └── LoyaltyPointsPaymentStrategy.java
│       │
│       ├── command/                         (NEW - Command Pattern)
│       │   ├── OrderCommand.java           (Command interface)
│       │   ├── OrderCommandInvoker.java    (Invoker)
│       │   ├── OrderManager.java           (Receiver + Subject)
│       │   ├── PlaceOrderCommand.java
│       │   ├── CancelOrderCommand.java
│       │   └── UpdateOrderStatusCommand.java
│       │
│       ├── composite/                       (From Lab 2)
│       ├── decorator/                       (From Lab 2)
│       ├── domain/                          (From Lab 1)
│       ├── factory/                         (From Lab 1)
│       └── models/                          (From Lab 1)
│
├── Lab2Report.md                            (Structural patterns report)
├── Lab3Report.md                            (This file)
└── README.md                                (Creational patterns report)
```

---

## Conclusions

This implementation successfully demonstrates **3 Behavioral Design Patterns** working in harmony with the existing Creational and Structural patterns:

### Behavioral Patterns Implemented:

1. **Observer Pattern**
   - Enables real-time notifications to multiple stakeholders
   - Loose coupling between order management and notification systems
   - Easy extensibility for new notification channels

2. **Strategy Pattern**
   - Flexible payment processing with runtime method selection
   - Clean encapsulation of payment algorithms
   - Easy addition of new payment types

3. **Command Pattern**
   - Full undo/redo support for order operations
   - Command history for audit and debugging
   - Decoupled operation execution from invocation

### Integration with Previous Labs:

| Lab | Pattern Type | Patterns |
|-----|--------------|----------|
| Lab 1 | Creational | Singleton, Prototype, Builder, Factory, Template Method |
| Lab 2 | Structural | Decorator, Composite, Facade |
| Lab 3 | Behavioral | Observer, Strategy, Command |

### Key Achievements:

✅ **Single Unified Client**: All functionality through `CoffeeApp.java`  
✅ **Seamless Integration**: Behavioral patterns work with existing architecture  
✅ **Real-World Applicability**: Notifications, payments, and undo are practical features  
✅ **Extensibility**: Easy to add new observers, strategies, and commands  
✅ **Maintainability**: Clear separation of concerns across all patterns  

