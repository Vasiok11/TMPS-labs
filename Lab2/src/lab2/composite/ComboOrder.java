package lab2.composite;

import java.util.ArrayList;
import java.util.List;

// Composite Pattern: Composite
public class ComboOrder implements OrderComponent {
    private final String comboName;
    private final List<OrderComponent> orders;
    private final double discount;

    public ComboOrder(String comboName, double discount) {
        this.comboName = comboName;
        this.orders = new ArrayList<>();
        this.discount = discount;
    }

    public void addOrder(OrderComponent order) {
        orders.add(order);
    }

    public void removeOrder(OrderComponent order) {
        orders.remove(order);
    }

    public List<OrderComponent> getOrders() {
        return new ArrayList<>(orders);
    }

    @Override
    public String getDescription() {
        return comboName + " (Contains " + getItemCount() + " items)";
    }

    @Override
    public double getTotalCost() {
        double total = 0.0;
        for (OrderComponent order : orders) {
            total += order.getTotalCost();
        }
        // Apply discount
        return total * (1.0 - discount);
    }

    @Override
    public void printOrder() {
        System.out.println(comboName + ":");
        for (OrderComponent order : orders) {
            order.printOrder();
        }
        if (discount > 0) {
            System.out.println("  Combo Discount: -" + (int)(discount * 100) + "%");
        }
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
