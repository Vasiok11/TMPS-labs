package lab2.facade;

import lab2.composite.ComboOrder;
import lab2.composite.OrderComponent;
import lab2.composite.SingleOrder;
import lab2.decorator.BaseCoffeeComponent;
import lab2.decorator.CaramelDrizzleDecorator;
import lab2.decorator.CoffeeComponent;
import lab2.decorator.ExtraShotDecorator;
import lab2.decorator.FlavorSyrupDecorator;
import lab2.decorator.WhippedCreamDecorator;
import lab2.domain.CoffeeOrderRequest;
import lab2.domain.OrderService;
import lab2.models.Coffee;
import lab2.models.CoffeeType;

// Facade Pattern: Simplifies complex subsystems
public class CoffeeShopFacade {
    private final OrderService orderService;

    public CoffeeShopFacade() {
        this.orderService = new OrderService();
    }

    /**
     * Places a simple coffee order and returns it wrapped as an OrderComponent.
     */
    public OrderComponent orderSingleCoffee(CoffeeType type, CoffeeOrderRequest request) {
        Coffee coffee = orderService.placeOrder(type, request);
        CoffeeComponent component = new BaseCoffeeComponent(coffee);
        return new SingleOrder(component);
    }

    /**
     * Places a coffee order with decorations applied.
     */
    public OrderComponent orderDecoratedCoffee(
            CoffeeType type,
            CoffeeOrderRequest request,
            boolean addWhippedCream,
            boolean addExtraShot,
            String syrupFlavor,
            boolean addCaramel) {
        
        Coffee coffee = orderService.placeOrder(type, request);
        CoffeeComponent component = new BaseCoffeeComponent(coffee);

        // Apply decorators based on preferences
        if (addExtraShot) {
            component = new ExtraShotDecorator(component);
        }
        if (syrupFlavor != null && !syrupFlavor.isEmpty()) {
            component = new FlavorSyrupDecorator(component, syrupFlavor);
        }
        if (addWhippedCream) {
            component = new WhippedCreamDecorator(component);
        }
        if (addCaramel) {
            component = new CaramelDrizzleDecorator(component);
        }

        return new SingleOrder(component);
    }

    /**
     * Creates a combo order with multiple items and a discount.
     */
    public ComboOrder createComboOrder(String comboName, double discount) {
        return new ComboOrder(comboName, discount);
    }

    /**
     * Prints the complete order details and total cost.
     */
    public void printOrderSummary(OrderComponent order) {
        System.out.println("\n========== ORDER SUMMARY ==========");
        order.printOrder();
        System.out.println("-----------------------------------");
        System.out.println("Total Items: " + order.getItemCount());
        System.out.println("Total Cost: $" + String.format("%.2f", order.getTotalCost()));
        System.out.println("===================================\n");
    }

    /**
     * Quick method to order a coffee with common decorations.
     */
    public OrderComponent orderPopularCoffee(CoffeeType type, CoffeeOrderRequest request) {
        Coffee coffee = orderService.placeOrder(type, request);
        CoffeeComponent component = new BaseCoffeeComponent(coffee);

        // Add popular decorations based on coffee type
        switch (type) {
            case CAPPUCCINO:
                component = new WhippedCreamDecorator(component);
                component = new CaramelDrizzleDecorator(component);
                break;
            case LATTE:
                component = new FlavorSyrupDecorator(component, "Vanilla");
                break;
            case ESPRESSO:
                component = new ExtraShotDecorator(component);
                break;
        }

        return new SingleOrder(component);
    }
}
