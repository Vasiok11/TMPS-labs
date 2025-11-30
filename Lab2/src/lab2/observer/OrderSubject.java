package lab2.observer;

// Observer Pattern: Subject Interface
public interface OrderSubject {
    void addObserver(OrderObserver observer);
    void removeObserver(OrderObserver observer);
    void notifyOrderPlaced(String orderId);
    void notifyOrderPreparing(String orderId);
    void notifyOrderReady(String orderId);
    void notifyOrderCancelled(String orderId);
}
