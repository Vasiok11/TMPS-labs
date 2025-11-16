# Lab#2  - Structural Design Patterns - Pascari Vasile

## Objectives
Extend Lab#2 by implementing **3 Structural Design Patterns** to enhance the coffee shop ordering system with dynamic customization, unified order handling, and simplified client interface.

## Task Requirements

✅ **Implement at least 3 structural design patterns** - The patterns should help perform tasks in the system  
✅ **Bury object creation into functionalities** - Creation mechanisms integrated within the system  
✅ **Single unified client** - Only one client for the whole system  

---

## Implemented Structural Design Patterns

### 1. Decorator Pattern

**Purpose:** Dynamically add features and enhancements to coffee orders without modifying the base coffee objects.

**Location:** `lab2/decorator/` package

**Implementation Structure:**

```java
// Component Interface
public interface CoffeeComponent {
    String getDescription();
    double getCost();
    String getDetails();
}

// Concrete Component
public class BaseCoffeeComponent implements CoffeeComponent {
    private final Coffee coffee;
    private final double basePrice;
    
    // Wraps a Coffee object and calculates base price
}

// Abstract Decorator
public abstract class CoffeeDecorator implements CoffeeComponent {
    protected final CoffeeComponent wrappedCoffee;
    // Delegates to wrapped component
}

// Concrete Decorators
public class WhippedCreamDecorator extends CoffeeDecorator {
    private static final double WHIPPED_CREAM_COST = 0.50;
    // Adds whipped cream to description and cost
}

public class ExtraShotDecorator extends CoffeeDecorator {
    private static final double EXTRA_SHOT_COST = 0.75;
    // Adds extra espresso shot
}

public class FlavorSyrupDecorator extends CoffeeDecorator {
    private static final double SYRUP_COST = 0.60;
    private final String flavor;
    // Adds flavored syrup (Vanilla, Hazelnut, Caramel, etc.)
}

public class CaramelDrizzleDecorator extends CoffeeDecorator {
    private static final double CARAMEL_COST = 0.45;
    // Adds caramel drizzle topping
}
```

**Benefits:**
- Add features dynamically at runtime without modifying coffee classes
- Stack multiple decorations in any order
- Each decorator independently adds cost and description
- Open/Closed Principle: open for extension, closed for modification
- Flexible pricing calculation

**Usage Example:**
```java
Coffee coffee = orderService.placeOrder(CoffeeType.LATTE, request);
CoffeeComponent component = new BaseCoffeeComponent(coffee);

// Stack decorators
component = new ExtraShotDecorator(component);
component = new FlavorSyrupDecorator(component, "Vanilla");
component = new WhippedCreamDecorator(component);
component = new CaramelDrizzleDecorator(component);

System.out.println(component.getDescription());
// Output: "Latte + Extra Shot + Vanilla Syrup + Whipped Cream + Caramel Drizzle"

System.out.println("Cost: $" + component.getCost());
// Output: "Cost: $5.80"
```

---

### 2. Composite Pattern

**Purpose:** Treat individual orders and combo orders uniformly, allowing clients to work with single items and collections through the same interface.

**Location:** `lab2/composite/` package

**Implementation Structure:**

```java
// Component Interface
public interface OrderComponent {
    String getDescription();
    double getTotalCost();
    void printOrder();
    int getItemCount();
}

// Leaf - Single Order
public class SingleOrder implements OrderComponent {
    private final CoffeeComponent coffee;
    
    @Override
    public double getTotalCost() {
        return coffee.getCost();
    }
    
    @Override
    public int getItemCount() {
        return 1;
    }
}

// Composite - Combo Order
public class ComboOrder implements OrderComponent {
    private final String comboName;
    private final List<OrderComponent> orders;
    private final double discount;
    
    public void addOrder(OrderComponent order) {
        orders.add(order);
    }
    
    @Override
    public double getTotalCost() {
        double total = 0.0;
        for (OrderComponent order : orders) {
            total += order.getTotalCost();
        }
        return total * (1.0 - discount); // Apply discount
    }
    
    @Override
    public int getItemCount() {
        int count = 0;
        for (OrderComponent order : orders) {
            count += order.getItemCount();
        }
        return count;
    }
}
```

**Benefits:**
- Uniform treatment of single and composite orders
- Recursive structure handles nested combos naturally
- Automatic discount application at combo level
- Easy to add or remove items from combos
- Simplifies client code through polymorphism

**Usage Example:**
```java
// Create combo with 15% discount
ComboOrder combo = new ComboOrder("Morning Bundle", 0.15);

// Add individual orders
combo.addOrder(new SingleOrder(espresso));
combo.addOrder(new SingleOrder(latte));
combo.addOrder(new SingleOrder(cappuccino));

// Treat combo same as single order
System.out.println("Total Items: " + combo.getItemCount());
System.out.println("Total Cost: $" + combo.getTotalCost());
combo.printOrder();

// Output:
// Morning Bundle:
//   - Espresso ($2.50)
//   - Latte ($3.50)
//   - Cappuccino ($3.00)
//   Combo Discount: -15%
// Total Cost: $7.65
```

---

### 3. Facade Pattern

**Purpose:** Provide a simplified, unified interface to the complex subsystem of factories, order services, decorators, and composites.

**Location:** `lab2/facade/` package

**Implementation Structure:**

```java
public class CoffeeShopFacade {
    private final OrderService orderService;

    public CoffeeShopFacade() {
        this.orderService = new OrderService();
    }

    // Simple single order
    public OrderComponent orderSingleCoffee(CoffeeType type, CoffeeOrderRequest request) {
        Coffee coffee = orderService.placeOrder(type, request);
        CoffeeComponent component = new BaseCoffeeComponent(coffee);
        return new SingleOrder(component);
    }

    // Order with custom decorations
    public OrderComponent orderDecoratedCoffee(
            CoffeeType type,
            CoffeeOrderRequest request,
            boolean addWhippedCream,
            boolean addExtraShot,
            String syrupFlavor,
            boolean addCaramel) {
        
        Coffee coffee = orderService.placeOrder(type, request);
        CoffeeComponent component = new BaseCoffeeComponent(coffee);

        // Apply decorators based on preferences
        if (addExtraShot) {
            component = new ExtraShotDecorator(component);
        }
        if (syrupFlavor != null && !syrupFlavor.isEmpty()) {
            component = new FlavorSyrupDecorator(component, syrupFlavor);
        }
        if (addWhippedCream) {
            component = new WhippedCreamDecorator(component);
        }
        if (addCaramel) {
            component = new CaramelDrizzleDecorator(component);
        }

        return new SingleOrder(component);
    }

    // Popular preset configurations
    public OrderComponent orderPopularCoffee(CoffeeType type, CoffeeOrderRequest request) {
        Coffee coffee = orderService.placeOrder(type, request);
        CoffeeComponent component = new BaseCoffeeComponent(coffee);

        // Apply popular decorations based on coffee type
        switch (type) {
            case CAPPUCCINO:
                component = new WhippedCreamDecorator(component);
                component = new CaramelDrizzleDecorator(component);
                break;
            case LATTE:
                component = new FlavorSyrupDecorator(component, "Vanilla");
                break;
            case ESPRESSO:
                component = new ExtraShotDecorator(component);
                break;
        }

        return new SingleOrder(component);
    }

    // Create combo order
    public ComboOrder createComboOrder(String comboName, double discount) {
        return new ComboOrder(comboName, discount);
    }

    // Print order summary
    public void printOrderSummary(OrderComponent order) {
        System.out.println("\n========== ORDER SUMMARY ==========");
        order.printOrder();
        System.out.println("-----------------------------------");
        System.out.println("Total Items: " + order.getItemCount());
        System.out.println("Total Cost: $" + String.format("%.2f", order.getTotalCost()));
        System.out.println("===================================\n");
    }
}
```

**Benefits:**
- Single, simple entry point for all ordering operations
- Hides complexity of factories, decorators, and composites
- Provides convenient preset methods
- Reduces client coupling to subsystems
- Makes system easier to use and understand
- Easier to maintain and modify internal implementations

**Client Simplification:**

**Before Facade (complex):**
```java
OrderService orderService = new OrderService();
Coffee coffee = orderService.placeOrder(type, request);
CoffeeComponent component = new BaseCoffeeComponent(coffee);
component = new ExtraShotDecorator(component);
component = new WhippedCreamDecorator(component);
OrderComponent order = new SingleOrder(component);
```

**After Facade (simple):**
```java
CoffeeShopFacade facade = new CoffeeShopFacade();
OrderComponent order = facade.orderDecoratedCoffee(
    type, request, 
    true,  // whipped cream
    true,  // extra shot
    null,  // no syrup
    false  // no caramel
);
```

---

## Single Unified Client

The `CoffeeApp.java` serves as the **single client** for the entire system, using only the `CoffeeShopFacade`:

```java
public final class CoffeeApp {
    // Single facade instance - only interface to the system
    private static final CoffeeShopFacade facade = new CoffeeShopFacade();

    public static void main(String[] args) {
        // All operations go through facade
        Scanner scanner = new Scanner(System.in);
        
        while (running) {
            printMainMenu();
            int choice = readChoice(scanner);
            
            switch (choice) {
                case 1:
                    orderSingleCoffee(scanner);    // Uses facade
                    break;
                case 2:
                    orderDecoratedCoffee(scanner); // Uses facade
                    break;
                case 3:
                    orderPopularCoffee(scanner);   // Uses facade
                    break;
                case 4:
                    orderCombo(scanner);           // Uses facade
                    break;
            }
        }
    }
}
```

**Key Points:**
- Client only imports and uses `CoffeeShopFacade`
- No direct access to factories, order service, or decorators
- All creation patterns buried within the facade
- Simple, clean, maintainable client code

---

## Pattern Integration

All patterns work together seamlessly:

**Order Creation Flow:**
```
1. Client → CoffeeShopFacade.orderDecoratedCoffee()
2. Facade → OrderService.placeOrder() (Factory patterns)
3. Facade → new BaseCoffeeComponent(coffee)
4. Facade → Apply decorators (Decorator pattern)
5. Facade → new SingleOrder(decorated) (Composite pattern)
6. Return → OrderComponent to client
```

**Combo Creation Flow:**
```
1. Client → facade.createComboOrder()
2. Client → facade.orderSingleCoffee() × N
3. Client → combo.addOrder() × N
4. Client → facade.printOrderSummary(combo)
5. Composite → Recursively calculates total with discount
```

---

## Example Interactions

### Example 1: Decorated Coffee Order

**User Input:**
```
=== Coffee Shop Menu ===
1. Order Single Coffee
2. Order Decorated Coffee (with toppings)
3. Order Popular Coffee (preset decorations)
4. Order Combo Deal
0. Exit
Select an option: 2

Select Coffee Type:
1. Espresso
2. Latte
3. Cappuccino
Choice: 2

Preferred size (S/M/L, blank for default): L
Milk choice (blank for recipe default): Oat
Add whipped cream? (y/N): y
Add extra shot? (y/N): y
Add flavor syrup? (Vanilla/Hazelnut/Caramel/none): Vanilla
Add caramel drizzle? (y/N): n
```

**Output:**
```
========== ORDER SUMMARY ==========
  - Latte + Extra Shot + Vanilla Syrup + Whipped Cream ($5.35)
-----------------------------------
Total Items: 1
Total Cost: $5.35
===================================
```

**Patterns Used:**
- **Facade**: `orderDecoratedCoffee()` simplifies the process
- **Decorator**: 3 decorators stacked (ExtraShot, FlavorSyrup, WhippedCream)
- **Composite**: SingleOrder wraps the decorated coffee
- **Factory**: LatteFactory creates the base coffee
- **Builder**: Coffee object constructed with customizations

---

### Example 2: Combo Order

**User Input:**
```
Select an option: 4
Enter combo name: Family Deal
Enter discount percentage (0-50): 20
How many items in combo? (1-5): 3

--- Item 1 ---
Choice: 1 (Espresso)
Size: S

--- Item 2 ---
Choice: 2 (Latte)
Size: M

--- Item 3 ---
Choice: 3 (Cappuccino)
Size: L
```

**Output:**
```
========== ORDER SUMMARY ==========
Family Deal:
  - Espresso ($2.00)
  - Latte ($3.50)
  - Cappuccino ($3.90)
  Combo Discount: -20%
-----------------------------------
Total Items: 3
Total Cost: $7.52
===================================
```

**Calculation:**
```
Base total: $2.00 + $3.50 + $3.90 = $9.40
Discount: 20% off
Final total: $9.40 × 0.80 = $7.52
```

**Patterns Used:**
- **Facade**: `createComboOrder()` and `orderSingleCoffee()` × 3
- **Composite**: ComboOrder contains 3 SingleOrders
- **Composite**: Recursive getTotalCost() with discount
- All creational patterns for creating each coffee

---

### Example 3: Popular Coffee (Preset)

**User Input:**
```
Select an option: 3
Choice: 3 (Cappuccino)
Size: M
```

**Output:**
```
(Popular preset decorations applied for Cappuccino)

========== ORDER SUMMARY ==========
  - Cappuccino + Whipped Cream + Caramel Drizzle ($3.95)
-----------------------------------
Total Items: 1
Total Cost: $3.95
===================================
```

**Preset Decorations:**
- **Espresso** → + Extra Shot
- **Latte** → + Vanilla Syrup
- **Cappuccino** → + Whipped Cream + Caramel Drizzle

**Patterns Used:**
- **Facade**: `orderPopularCoffee()` applies preset decorations
- **Decorator**: Automatic decoration based on coffee type
- **Strategy**: Different decorations per coffee type

---

## How Requirements Are Met

### ✅ Requirement 1: At Least 3 Structural Design Patterns

**Implemented:**
1. **Decorator Pattern** - 7 classes (1 interface, 1 concrete component, 1 abstract decorator, 4 concrete decorators)
2. **Composite Pattern** - 3 classes (1 interface, 1 leaf, 1 composite)
3. **Facade Pattern** - 1 class (unified interface to subsystems)

### ✅ Requirement 2: Patterns Help Perform Tasks

- **Decorator**: Dynamically adds toppings and calculates prices
- **Composite**: Handles single/combo orders uniformly with discounts
- **Facade**: Simplifies ordering process for clients

### ✅ Requirement 3: Object Creation Buried into Functionalities

- Factory patterns completely hidden behind facade
- Client never directly instantiates factories
- All creation logic encapsulated within facade methods
- Client uses simple method calls, not complex object graphs

### ✅ Requirement 4: Single Client for Whole System

- `CoffeeApp.java` is the **only** client
- Uses **only** `CoffeeShopFacade` for all operations
- No direct access to factories, decorators, or composites
- Clean separation between client and implementation

---

## Project Structure

```
Lab2/
├── src/
│   └── lab2/
│       ├── client/
│       │   └── CoffeeApp.java          (Single unified client)
│       ├── facade/                      (NEW - Facade Pattern)
│       │   └── CoffeeShopFacade.java
│       ├── decorator/                   (NEW - Decorator Pattern)
│       │   ├── CoffeeComponent.java
│       │   ├── BaseCoffeeComponent.java
│       │   ├── CoffeeDecorator.java
│       │   ├── WhippedCreamDecorator.java
│       │   ├── ExtraShotDecorator.java
│       │   ├── FlavorSyrupDecorator.java
│       │   └── CaramelDrizzleDecorator.java
│       ├── composite/                   (NEW - Composite Pattern)
│       │   ├── OrderComponent.java
│       │   ├── SingleOrder.java
│       │   └── ComboOrder.java
│       ├── domain/
│       │   ├── CoffeeOrderRequest.java (Builder Pattern)
│       │   ├── OrderService.java       (Strategy Pattern)
│       │   └── RecipeRegistry.java     (Singleton Pattern)
│       ├── factory/
│       │   ├── CoffeeFactory.java      (Factory Method Pattern)
│       │   ├── CoffeeShop.java         (Template Method Pattern)
│       │   ├── EspressoFactory.java
│       │   ├── EspressoShop.java
│       │   ├── LatteFactory.java
│       │   ├── LatteShop.java
│       │   ├── CappuccinoFactory.java
│       │   ├── CappuccinoShop.java
│       │   └── RecipeBackedFactory.java
│       └── models/
│           ├── Coffee.java             (Builder Pattern)
│           ├── CoffeeRecipe.java       (Prototype Pattern)
│           ├── CoffeeType.java
│           └── Size.java
└── STRUCTURAL_PATTERNS.md              (This file)
```

---

## Conclusions

This implementation successfully demonstrates **3 Structural Design Patterns** working in harmony with the existing **Creational Patterns** from Lab#2:

**Structural Patterns Added:**
1. **Decorator Pattern** - Dynamic feature addition without modification
2. **Composite Pattern** - Uniform treatment of single/combo orders
3. **Facade Pattern** - Simplified unified interface

**Integration with Existing Creational Patterns:**
- **Singleton** (RecipeRegistry) - Provides recipe templates
- **Prototype** (CoffeeRecipe) - Clones recipes efficiently
- **Builder** (Coffee, CoffeeOrderRequest) - Constructs complex objects
- **Factory Method** (CoffeeFactory, CoffeeShop) - Encapsulates creation logic

**Key Achievements:**

1. ✅ **Single Unified Client**: CoffeeApp uses only CoffeeShopFacade
2. ✅ **Buried Object Creation**: All factories hidden behind facade
3. ✅ **Dynamic Customization**: Decorators allow flexible enhancements
4. ✅ **Uniform Order Handling**: Composite treats all orders identically
5. ✅ **Simplified Interface**: Facade reduces complexity dramatically
6. ✅ **Extensibility**: Easy to add new decorators, combos, or coffee types
7. ✅ **Maintainability**: Clear separation of concerns across patterns

**Real-World Benefits:**

- **For Users**: Intuitive ordering with flexible customization options
- **For Developers**: Clean architecture with well-separated concerns
- **For Business**: Easy to add new products, promotions, and combo deals
- **For Maintenance**: Clear structure simplifies debugging and updates

The modular design demonstrates how structural patterns complement creational patterns to build robust, flexible, and maintainable systems. The facade provides a clean separation between client and implementation, while decorators and composites handle runtime variation elegantly without modifying existing code.

**Design Principles Demonstrated:**
- **Single Responsibility Principle** - Each class has one clear purpose
- **Open/Closed Principle** - Open for extension, closed for modification
- **Liskov Substitution Principle** - Components are interchangeable
- **Interface Segregation Principle** - Focused interfaces
- **Dependency Inversion Principle** - Depend on abstractions, not concretions

This implementation showcases professional software engineering practices and effective use of design patterns to solve real-world problems.
