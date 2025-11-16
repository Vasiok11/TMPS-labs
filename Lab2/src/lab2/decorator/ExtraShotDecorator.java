package lab2.decorator;

// Decorator Pattern: Concrete Decorator
public class ExtraShotDecorator extends CoffeeDecorator {
    private static final double EXTRA_SHOT_COST = 0.75;

    public ExtraShotDecorator(CoffeeComponent wrappedCoffee) {
        super(wrappedCoffee);
    }

    @Override
    public String getDescription() {
        return wrappedCoffee.getDescription() + " + Extra Shot";
    }

    @Override
    public double getCost() {
        return wrappedCoffee.getCost() + EXTRA_SHOT_COST;
    }

    @Override
    public String getDetails() {
        return wrappedCoffee.getDetails() + " [+Extra Shot $" + EXTRA_SHOT_COST + "]";
    }
}
