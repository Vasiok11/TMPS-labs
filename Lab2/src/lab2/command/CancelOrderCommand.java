package lab2.command;

// Command Pattern: Concrete Command
public class CancelOrderCommand implements OrderCommand {
    private final OrderManager orderManager;
    private final String orderId;
    private OrderManager.OrderEntry cancelledOrder;

    public CancelOrderCommand(OrderManager orderManager, String orderId) {
        this.orderManager = orderManager;
        this.orderId = orderId;
    }

    @Override
    public void execute() {
        cancelledOrder = orderManager.getOrderEntry(orderId);
        if (cancelledOrder != null) {
            orderManager.cancelOrder(orderId);
            System.out.println("[COMMAND] Order " + orderId + " has been cancelled.");
        } else {
            System.out.println("[COMMAND] Order " + orderId + " not found.");
        }
    }

    @Override
    public void undo() {
        if (cancelledOrder != null) {
            orderManager.restoreOrder(orderId, cancelledOrder);
            System.out.println("[COMMAND] Order " + orderId + " has been restored.");
        }
    }

    @Override
    public String getDescription() {
        return "Cancel order: " + orderId;
    }
}
