package lab2.decorator;

// Decorator Pattern: Concrete Decorator
public class FlavorSyrupDecorator extends CoffeeDecorator {
    private static final double SYRUP_COST = 0.60;
    private final String flavor;

    public FlavorSyrupDecorator(CoffeeComponent wrappedCoffee, String flavor) {
        super(wrappedCoffee);
        this.flavor = flavor;
    }

    @Override
    public String getDescription() {
        return wrappedCoffee.getDescription() + " + " + flavor + " Syrup";
    }

    @Override
    public double getCost() {
        return wrappedCoffee.getCost() + SYRUP_COST;
    }

    @Override
    public String getDetails() {
        return wrappedCoffee.getDetails() + " [+" + flavor + " Syrup $" + SYRUP_COST + "]";
    }

    public String getFlavor() {
        return flavor;
    }
}
