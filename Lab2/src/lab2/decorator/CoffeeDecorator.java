package lab2.decorator;

// Decorator Pattern: Abstract Decorator
public abstract class CoffeeDecorator implements CoffeeComponent {
    protected final CoffeeComponent wrappedCoffee;

    protected CoffeeDecorator(CoffeeComponent wrappedCoffee) {
        this.wrappedCoffee = wrappedCoffee;
    }

    @Override
    public String getDescription() {
        return wrappedCoffee.getDescription();
    }

    @Override
    public double getCost() {
        return wrappedCoffee.getCost();
    }

    @Override
    public String getDetails() {
        return wrappedCoffee.getDetails();
    }
}
