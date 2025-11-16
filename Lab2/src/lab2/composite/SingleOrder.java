package lab2.composite;

import lab2.decorator.CoffeeComponent;

// Composite Pattern: Leaf
public class SingleOrder implements OrderComponent {
    private final CoffeeComponent coffee;

    public SingleOrder(CoffeeComponent coffee) {
        this.coffee = coffee;
    }

    @Override
    public String getDescription() {
        return coffee.getDescription();
    }

    @Override
    public double getTotalCost() {
        return coffee.getCost();
    }

    @Override
    public void printOrder() {
        System.out.println("  - " + coffee.getDescription() + " ($" + String.format("%.2f", coffee.getCost()) + ")");
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public CoffeeComponent getCoffee() {
        return coffee;
    }
}
