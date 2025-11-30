package lab2.observer;

import lab2.composite.OrderComponent;

// Observer Pattern: Observer Interface
public interface OrderObserver {
    void onOrderPlaced(OrderComponent order, String orderId);
    void onOrderPreparing(String orderId);
    void onOrderReady(String orderId);
    void onOrderCancelled(String orderId);
}
