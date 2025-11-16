package lab2.decorator;

import lab2.models.Coffee;

// Decorator Pattern: Concrete Component
public class BaseCoffeeComponent implements CoffeeComponent {
    private final Coffee coffee;
    private final double basePrice;

    public BaseCoffeeComponent(Coffee coffee) {
        this.coffee = coffee;
        this.basePrice = calculateBasePrice();
    }

    private double calculateBasePrice() {
        double price = 0.0;
        
        // Base price by coffee type
        String name = coffee.getName().toLowerCase();
        if (name.contains("espresso")) {
            price = 2.50;
        } else if (name.contains("latte")) {
            price = 3.50;
        } else if (name.contains("cappuccino")) {
            price = 3.00;
        }

        // Size adjustment
        switch (coffee.getSize()) {
            case SMALL:
                price *= 0.8;
                break;
            case LARGE:
                price *= 1.3;
                break;
            case MEDIUM:
            default:
                break;
        }

        return price;
    }

    @Override
    public String getDescription() {
        return coffee.getName();
    }

    @Override
    public double getCost() {
        return basePrice;
    }

    @Override
    public String getDetails() {
        return coffee.toString();
    }

    public Coffee getCoffee() {
        return coffee;
    }
}
