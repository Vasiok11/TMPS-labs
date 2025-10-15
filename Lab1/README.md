# Lab#0 - SOLID Principles - Pascari Vasile
## Objectives
Demonstrate three core SOLID principles (Single Responsibility, Open/Closed, Dependency Inversion) in a simple, working coffee shop ordering system.

## Used Principles

### 1. Single Responsibility Principle (SRP)
**CoffeeOrder.java** has one clear responsibility: representing and describing a coffee order. It doesn't handle pricing, storage, or notifications—just data and its presentation.

```java
// Demonstrates SRP: this class is only responsible for representing a coffee order.
public class CoffeeOrder {
    private final String beverage;
    private final String size;
    private final List<String> extras;

    public String describe() {
        if (extras == null || extras.isEmpty()) {
            return capitalize(size) + " " + capitalize(beverage) + " with no extras.";
        }
        String extrasPart = extras.stream()
                .map(this::capitalize)
                .collect(Collectors.joining(", "));
        return capitalize(size) + " " + capitalize(beverage) + " with " + extrasPart + ".";
    }
}
```

### 2. Open/Closed Principle (OCP)
**PriceCalculator.java** is open for extension but closed for modification. New pricing rules can be added by injecting new modifier functions without changing the calculator's core logic.

```java
// Demonstrates OCP: new pricing modifiers can be registered without changing this class.
public class PriceCalculator {
    private final Map<String, Double> basePrices;
    private final List<BiFunction<CoffeeOrder, Double, Double>> modifiers;

    public double calculate(CoffeeOrder order) {
        Double startingPrice = basePrices.get(order.getBeverage());
        if (startingPrice == null) {
            throw new IllegalArgumentException("Unknown beverage: " + order.getBeverage());
        }
        double total = startingPrice;
        for (BiFunction<CoffeeOrder, Double, Double> modifier : modifiers) {
            total = modifier.apply(order, total);
        }
        return Math.round(total * 100.0) / 100.0;
    }
}
```

Example modifiers that extend functionality without editing the class:
```java
public static double applySizeModifier(CoffeeOrder order, Double runningTotal) {
    Map<String, Double> sizeAdjustments = new HashMap<>();
    sizeAdjustments.put("small", 0.0);
    sizeAdjustments.put("medium", 0.5);
    sizeAdjustments.put("large", 1.0);
    
    Double adjustment = sizeAdjustments.get(order.getSize());
    if (adjustment == null) {
        throw new IllegalArgumentException("Unknown size: " + order.getSize());
    }
    return runningTotal + adjustment;
}
```

### 3. Dependency Inversion Principle (DIP)
**CoffeeService.java** depends on abstractions (functional interfaces) rather than concrete implementations. The service doesn't know or care whether orders are saved to memory, disk, or a database—it just calls the injected `saveOrder` function.

```java
// Demonstrates DIP: depends on abstractions (functional interfaces) instead of concrete implementations.
public class CoffeeService {
    private final PriceCalculator calculator;
    private final BiConsumer<CoffeeOrder, Double> saveOrder;
    private final Consumer<String> notify;

    public CoffeeService(PriceCalculator calculator, 
                         BiConsumer<CoffeeOrder, Double> saveOrder, 
                         Consumer<String> notify) {
        this.calculator = calculator;
        this.saveOrder = saveOrder;
        this.notify = notify;
    }

    public double process(CoffeeOrder order) {
        double total = calculator.calculate(order);
        saveOrder.accept(order, total);
        notify.accept(String.format("Order ready: %s Total: $%.2f", order.describe(), total));
        return total;
    }
}
```

## Implementation

The system models a simple coffee shop where customers can order beverages with different sizes and extras. The architecture separates concerns into distinct classes: `CoffeeOrder` handles order data, `PriceCalculator` computes pricing through pluggable modifiers, and `CoffeeService` orchestrates the workflow while depending only on injected abstractions. This design makes the system easy to extend (new pricing rules, storage backends, notification channels) without modifying existing code.

### Main Application Wiring

```java
public class Main {
    public static void main(String[] args) {
        Map<String, Double> basePrices = new HashMap<>();
        basePrices.put("espresso", 2.5);
        basePrices.put("latte", 3.5);
        basePrices.put("cappuccino", 3.8);

        PriceCalculator calculator = new PriceCalculator(
                basePrices,
                Arrays.asList(
                        PriceCalculator::applySizeModifier,
                        PriceCalculator::applyExtrasModifier
                )
        );

        List<String> storedOrders = new ArrayList<>();

        BiConsumer<CoffeeOrder, Double> saveToMemory = (order, total) ->
                storedOrders.add(order.describe() + " Total: $" + String.format("%.2f", total));

        Consumer<String> notifyConsole = System.out::println;

        CoffeeService service = new CoffeeService(calculator, saveToMemory, notifyConsole);

        // ... CLI interaction code ...
    }
}
```

## Results

The application successfully demonstrates SOLID principles while maintaining simplicity:

**Example output:**
```
Choose beverage (espresso/latte/cappuccino): latte
Choose size (small/medium/large): large
Extras (comma separated, blank for none): oat, vanilla
Order ready: Large Latte with Oat, Vanilla. Total: $5.60
Stored 1 order(s). Last total: $5.60
```

**Pricing Breakdown:**
- Base latte: $3.50
- Large size adjustment: +$1.00
- Oat milk: +$0.50
- Vanilla syrup: +$0.60
- **Total: $5.60**

## Conclusions

This implementation successfully demonstrates three SOLID principles in a minimal, working application:

- **SRP** keeps each class focused on a single concern, making the code easier to understand and maintain
- **OCP** allows new pricing strategies to be added without modifying existing calculator logic
- **DIP** enables flexible collaboration patterns where implementations can be swapped at runtime

The coffee shop domain proved simple enough to keep the code concise while still providing meaningful examples of each principle in action. The modular design would easily accommodate extensions like database persistence, email notifications, or loyalty program discounts without breaking existing functionality.
