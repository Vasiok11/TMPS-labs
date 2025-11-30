package lab2.command;

import lab2.composite.OrderComponent;

// Command Pattern: Concrete Command
public class PlaceOrderCommand implements OrderCommand {
    private final OrderManager orderManager;
    private final OrderComponent order;
    private String orderId;

    public PlaceOrderCommand(OrderManager orderManager, OrderComponent order) {
        this.orderManager = orderManager;
        this.order = order;
    }

    @Override
    public void execute() {
        orderId = orderManager.addOrder(order);
        System.out.println("[COMMAND] Order placed with ID: " + orderId);
    }

    @Override
    public void undo() {
        if (orderId != null) {
            orderManager.removeOrder(orderId);
            System.out.println("[COMMAND] Order " + orderId + " has been undone (removed).");
        }
    }

    @Override
    public String getDescription() {
        return "Place order: " + order.getDescription();
    }

    public String getOrderId() {
        return orderId;
    }
}
