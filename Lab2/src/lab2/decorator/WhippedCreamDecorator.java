package lab2.decorator;

// Decorator Pattern: Concrete Decorator
public class WhippedCreamDecorator extends CoffeeDecorator {
    private static final double WHIPPED_CREAM_COST = 0.50;

    public WhippedCreamDecorator(CoffeeComponent wrappedCoffee) {
        super(wrappedCoffee);
    }

    @Override
    public String getDescription() {
        return wrappedCoffee.getDescription() + " + Whipped Cream";
    }

    @Override
    public double getCost() {
        return wrappedCoffee.getCost() + WHIPPED_CREAM_COST;
    }

    @Override
    public String getDetails() {
        return wrappedCoffee.getDetails() + " [+Whipped Cream $" + WHIPPED_CREAM_COST + "]";
    }
}
