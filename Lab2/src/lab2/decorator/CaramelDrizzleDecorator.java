package lab2.decorator;

// Decorator Pattern: Concrete Decorator
public class CaramelDrizzleDecorator extends CoffeeDecorator {
    private static final double CARAMEL_COST = 0.45;

    public CaramelDrizzleDecorator(CoffeeComponent wrappedCoffee) {
        super(wrappedCoffee);
    }

    @Override
    public String getDescription() {
        return wrappedCoffee.getDescription() + " + Caramel Drizzle";
    }

    @Override
    public double getCost() {
        return wrappedCoffee.getCost() + CARAMEL_COST;
    }

    @Override
    public String getDetails() {
        return wrappedCoffee.getDetails() + " [+Caramel Drizzle $" + CARAMEL_COST + "]";
    }
}
