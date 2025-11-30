package lab2.observer;

import lab2.composite.OrderComponent;

// Observer Pattern: Concrete Observer
public class CustomerNotificationObserver implements OrderObserver {
    private final String customerName;

    public CustomerNotificationObserver(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public void onOrderPlaced(OrderComponent order, String orderId) {
        System.out.println("[SMS to " + customerName + "] Your order #" + orderId + 
            " has been placed! Total: $" + String.format("%.2f", order.getTotalCost()));
    }

    @Override
    public void onOrderPreparing(String orderId) {
        System.out.println("[SMS to " + customerName + "] Your order #" + orderId + 
            " is now being prepared by our barista!");
    }

    @Override
    public void onOrderReady(String orderId) {
        System.out.println("[SMS to " + customerName + "] Your order #" + orderId + 
            " is ready for pickup! Enjoy your coffee!");
    }

    @Override
    public void onOrderCancelled(String orderId) {
        System.out.println("[SMS to " + customerName + "] Your order #" + orderId + 
            " has been cancelled. Refund will be processed shortly.");
    }

    public String getCustomerName() {
        return customerName;
    }
}
