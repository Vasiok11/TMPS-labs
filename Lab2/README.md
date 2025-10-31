# Lab#2 - Creational Design Patterns - Pascari Vasile

## Objectives
Demonstrate creational design patterns (Singleton, Prototype, Builder, Factory, and Template Method) in a coffee shop ordering system that allows for flexible object creation and customization.

## Used Design Patterns

### 1. Singleton Pattern
**RecipeRegistry.java** ensures only one instance of the recipe registry exists throughout the application, providing centralized access to all coffee recipes.

```java
// Demonstrates Singleton: ensures a single shared instance of the recipe registry
public final class RecipeRegistry {
    private static final RecipeRegistry INSTANCE = new RecipeRegistry();
    private final Map<CoffeeType, CoffeeRecipe> recipes = new EnumMap<>(CoffeeType.class);

    private RecipeRegistry() {
        loadDefaultRecipes();
    }

    public static RecipeRegistry getInstance() {
        return INSTANCE;
    }

    public CoffeeRecipe lookup(CoffeeType type) {
        CoffeeRecipe recipe = recipes.get(type);
        if (recipe == null) {
            throw new IllegalArgumentException("No recipe registered for type: " + type);
        }
        return recipe.clone(); // Returns a clone to maintain prototype pattern
    }
}
```

**Benefits:**
- Guarantees single point of access to recipe data
- Eager initialization ensures thread safety
- Prevents duplicate recipe registries

### 2. Prototype Pattern
**CoffeeRecipe.java** implements the Prototype pattern by providing cloneable recipe blueprints, allowing quick creation of pre-configured beverages without expensive initialization.

```java
// Demonstrates Prototype: cloneable recipe blueprints
public class CoffeeRecipe implements Cloneable {
    private final String name;
    private final Size defaultSize;
    private final String defaultMilk;
    private final List<String> defaultAddOns;

    public CoffeeRecipe(String name, Size defaultSize, String defaultMilk, 
                        List<String> defaultAddOns) {
        this.name = name;
        this.defaultSize = defaultSize;
        this.defaultMilk = defaultMilk;
        this.defaultAddOns = List.copyOf(defaultAddOns);
    }

    @Override
    public CoffeeRecipe clone() {
        return new CoffeeRecipe(name, defaultSize, defaultMilk, defaultAddOns);
    }
}
```

**Usage in Registry:**
```java
private void loadDefaultRecipes() {
    recipes.put(
        CoffeeType.ESPRESSO,
        new CoffeeRecipe("Espresso", Size.SMALL, "None", List.of())
    );
    recipes.put(
        CoffeeType.LATTE,
        new CoffeeRecipe("Latte", Size.MEDIUM, "Oat", List.of("Vanilla Syrup"))
    );
    recipes.put(
        CoffeeType.CAPPUCCINO,
        new CoffeeRecipe("Cappuccino", Size.MEDIUM, "Whole", List.of("Cocoa Powder"))
    );
}
```

### 3. Builder Pattern
**Coffee.java** and **CoffeeOrderRequest.java** implement the Builder pattern to construct complex objects with many optional parameters through a fluent, readable interface.

```java
// Demonstrates Builder: fluent construction of complex coffee objects
public final class Coffee {
    private final String name;
    private final Size size;
    private final String milkType;
    private final List<String> addOns;
    private final boolean takeaway;

    private Coffee(Builder builder) {
        this.name = builder.name;
        this.size = builder.size;
        this.milkType = builder.milkType;
        this.addOns = List.copyOf(builder.addOns);
        this.takeaway = builder.takeaway;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private Size size = Size.MEDIUM;
        private String milkType = "Whole";
        private final List<String> addOns = new ArrayList<>();
        private boolean takeaway;

        public Builder name(String name) {
            this.name = Objects.requireNonNull(name, "name");
            return this;
        }

        public Builder size(Size size) {
            this.size = Objects.requireNonNull(size, "size");
            return this;
        }

        public Builder milkType(String milkType) {
            this.milkType = Objects.requireNonNull(milkType, "milkType");
            return this;
        }

        public Builder addOn(String addOn) {
            this.addOns.add(Objects.requireNonNull(addOn, "addOn"));
            return this;
        }

        public Builder addOns(List<String> addOns) {
            this.addOns.addAll(Objects.requireNonNull(addOns, "addOns"));
            return this;
        }

        public Builder takeaway(boolean takeaway) {
            this.takeaway = takeaway;
            return this;
        }

        public Coffee build() {
            return new Coffee(this);
        }
    }
}
```

**Usage Example:**
```java
Coffee coffee = Coffee.builder()
    .name("Latte")
    .size(Size.LARGE)
    .milkType("Oat")
    .addOn("Vanilla Syrup")
    .addOn("Latte Art")
    .takeaway(false)
    .build();
```

### 4. Factory Pattern
**CoffeeFactory.java** interface with concrete implementations (`EspressoFactory`, `LatteFactory`, `CappuccinoFactory`) encapsulates the creation logic for different coffee types.

```java
// Demonstrates Factory: abstract factory interface for coffee creation
public interface CoffeeFactory {
    Coffee brewCoffee(CoffeeOrderRequest request);
    CoffeeType supportedType();
}
```

**Concrete Factory Example:**
```java
public class LatteFactory extends RecipeBackedFactory {
    @Override
    public CoffeeType supportedType() {
        return CoffeeType.LATTE;
    }

    @Override
    protected void applySpecialTouch(Coffee.Builder builder, 
                                    CoffeeOrderRequest request, 
                                    CoffeeRecipe recipe) {
        // Lattes default to latte art sprinkle when enjoyed in house
        if (!request.takeaway()) {
            builder.addOn("Latte Art");
        }
    }
}
```

### 5. Template Method Pattern
**RecipeBackedFactory.java** defines the skeleton of the coffee brewing algorithm, allowing subclasses to customize specific steps without changing the overall structure.

```java
// Demonstrates Template Method: reusable brewing template with customizable hooks
abstract class RecipeBackedFactory implements CoffeeFactory {
    private final RecipeRegistry registry = RecipeRegistry.getInstance();

    @Override
    public Coffee brewCoffee(CoffeeOrderRequest request) {
        // Template method defining the algorithm structure
        CoffeeRecipe recipe = registry.lookup(supportedType());

        Coffee.Builder builder = Coffee.builder()
            .name(recipe.getName())
            .size(request.size().orElse(recipe.getDefaultSize()))
            .milkType(request.milkType().orElse(recipe.getDefaultMilk()))
            .takeaway(request.takeaway());

        if (!recipe.getDefaultAddOns().isEmpty()) {
            builder.addOns(recipe.getDefaultAddOns());
        }
        if (!request.addOns().isEmpty()) {
            builder.addOns(request.addOns());
        }

        // Hook method for subclass customization
        applySpecialTouch(builder, request, recipe);
        return builder.build();
    }

    // Hook method - subclasses can override for custom behavior
    protected void applySpecialTouch(Coffee.Builder builder, 
                                    CoffeeOrderRequest request, 
                                    CoffeeRecipe recipe) {
        // default does nothing
    }

    @Override
    public abstract CoffeeType supportedType();
}
```

**Factory Specializations:**
```java
// Espresso keeps milk minimal
public class EspressoFactory extends RecipeBackedFactory {
    @Override
    protected void applySpecialTouch(Coffee.Builder builder, 
                                    CoffeeOrderRequest request, 
                                    CoffeeRecipe recipe) {
        if (request.milkType().isEmpty()) {
            builder.milkType("None");
        }
    }
}

// Cappuccino upsizes for takeaway
public class CappuccinoFactory extends RecipeBackedFactory {
    @Override
    protected void applySpecialTouch(Coffee.Builder builder, 
                                    CoffeeOrderRequest request, 
                                    CoffeeRecipe recipe) {
        if (request.takeaway() && request.size().isEmpty()) {
            builder.size(Size.LARGE);
        }
    }
}
```

### 6. Strategy Pattern (with Factory Registration)
**OrderService.java** coordinates coffee orders by delegating creation to registered factories, effectively implementing a strategy pattern for object creation.

```java
// Demonstrates Strategy: delegates creation to appropriate factory
public class OrderService {
    private final Map<CoffeeType, CoffeeFactory> factories = new EnumMap<>(CoffeeType.class);

    public void registerFactory(CoffeeFactory factory) {
        factories.put(factory.supportedType(), factory);
    }

    public Coffee placeOrder(CoffeeType type, CoffeeOrderRequest request) {
        CoffeeFactory factory = factories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("No factory registered for type: " + type);
        }
        return factory.brewCoffee(request);
    }
}
```

## Implementation

The system models a coffee shop with an interactive menu where customers can customize their beverages. The architecture is organized into four main packages:

- **`lab2.client`** – Contains `CoffeeApp`, the interactive console application
- **`lab2.domain`** – Business logic including `OrderService`, `CoffeeOrderRequest`, and `RecipeRegistry`
- **`lab2.factory`** – Factory implementations for coffee creation
- **`lab2.models`** – Core domain entities (`Coffee`, `CoffeeRecipe`, `CoffeeType`, `Size`)

### Application Wiring

```java
public class CoffeeApp {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        orderService.registerFactory(new EspressoFactory());
        orderService.registerFactory(new LatteFactory());
        orderService.registerFactory(new CappuccinoFactory());

        try (Scanner scanner = new Scanner(System.in)) {
            scanner.useLocale(Locale.US);
            boolean running = true;
            while (running) {
                printMenu();
                int choice = readChoice(scanner);
                if (choice == 0) {
                    running = false;
                    continue;
                }

                CoffeeType type = resolveType(choice);
                if (type == null) {
                    System.out.println("Please choose a valid option.");
                    continue;
                }

                CoffeeOrderRequest request = gatherCustomization(scanner);
                Coffee coffee = orderService.placeOrder(type, request);
                System.out.println("\nPrepared: " + coffee + '\n');
            }
        }

        System.out.println("Goodbye!");
    }
}
```

## Results

The application demonstrates all design patterns working together in a cohesive system:

**Example Interaction:**
```
=== Coffee Shop Menu ===
1. Espresso
2. Latte
3. Cappuccino
0. Exit
Select an option: 2
Preferred size (S/M/L, blank for default): L
Milk choice (blank for recipe default): Oat
Extra add-ons separated by comma (blank for none): Caramel
Takeaway? (y/n): n

Prepared: Coffee{name='Latte', size=LARGE, milkType='Oat', 
                 addOns=[Vanilla Syrup, Caramel, Latte Art], takeaway=false}
```

**Pattern Interactions:**
1. **Singleton** `RecipeRegistry` provides the base recipe for Latte
2. **Prototype** clones the recipe to avoid modifying the original
3. **Factory** `LatteFactory` handles latte-specific brewing logic
4. **Template Method** defines the common brewing steps with latte-specific customization
5. **Builder** constructs the final `Coffee` object with all customizations
6. **Strategy** `OrderService` delegates to the correct factory based on coffee type

## Conclusions

This implementation successfully demonstrates six design patterns working in harmony:

- **Singleton** ensures centralized recipe management and prevents resource duplication
- **Prototype** enables efficient object creation by cloning pre-configured templates
- **Builder** provides a clean, fluent interface for constructing complex objects with many optional parameters
- **Factory** encapsulates creation logic and enables easy extension with new coffee types
- **Template Method** promotes code reuse while allowing customization at specific points
- **Strategy** enables runtime selection of creation algorithms based on coffee type

The coffee shop domain effectively showcases how creational patterns solve real-world problems:
- Managing shared resources (Singleton)
- Efficient object creation (Prototype)
- Complex object construction (Builder)
- Encapsulating creation logic (Factory)
- Reusing algorithms with variation points (Template Method)
- Runtime algorithm selection (Strategy)

The modular design makes it trivial to add new coffee types, customization options, or brewing techniques without modifying existing code, demonstrating the extensibility benefits of well-applied design patterns.
