package lab2.observer;

import lab2.composite.OrderComponent;

// Observer Pattern: Concrete Observer
public class KitchenDisplayObserver implements OrderObserver {
    private static final String DISPLAY_PREFIX = "[KITCHEN DISPLAY]";

    @Override
    public void onOrderPlaced(OrderComponent order, String orderId) {
        System.out.println(DISPLAY_PREFIX + " NEW ORDER #" + orderId);
        System.out.println(DISPLAY_PREFIX + " Items: " + order.getDescription());
        System.out.println(DISPLAY_PREFIX + " -----------------------");
    }

    @Override
    public void onOrderPreparing(String orderId) {
        System.out.println(DISPLAY_PREFIX + " Order #" + orderId + " -> IN PROGRESS");
    }

    @Override
    public void onOrderReady(String orderId) {
        System.out.println(DISPLAY_PREFIX + " Order #" + orderId + " -> COMPLETED ✓");
    }

    @Override
    public void onOrderCancelled(String orderId) {
        System.out.println(DISPLAY_PREFIX + " Order #" + orderId + " -> CANCELLED ✗");
    }
}
