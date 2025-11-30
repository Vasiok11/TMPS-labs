package lab2.command;

// Command Pattern: Concrete Command
public class UpdateOrderStatusCommand implements OrderCommand {
    private final OrderManager orderManager;
    private final String orderId;
    private final OrderManager.OrderStatus newStatus;
    private OrderManager.OrderStatus previousStatus;

    public UpdateOrderStatusCommand(OrderManager orderManager, String orderId, 
                                     OrderManager.OrderStatus newStatus) {
        this.orderManager = orderManager;
        this.orderId = orderId;
        this.newStatus = newStatus;
    }

    @Override
    public void execute() {
        previousStatus = orderManager.getOrderStatus(orderId);
        if (previousStatus != null) {
            orderManager.updateOrderStatus(orderId, newStatus);
            System.out.println("[COMMAND] Order " + orderId + " status updated: " + 
                previousStatus + " -> " + newStatus);
        } else {
            System.out.println("[COMMAND] Order " + orderId + " not found.");
        }
    }

    @Override
    public void undo() {
        if (previousStatus != null) {
            orderManager.updateOrderStatus(orderId, previousStatus);
            System.out.println("[COMMAND] Order " + orderId + " status reverted: " + 
                newStatus + " -> " + previousStatus);
        }
    }

    @Override
    public String getDescription() {
        return "Update order " + orderId + " status to " + newStatus;
    }
}
