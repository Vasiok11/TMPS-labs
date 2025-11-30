package lab2.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lab2.composite.OrderComponent;
import lab2.observer.OrderObserver;
import lab2.observer.OrderSubject;

// Command Pattern: Receiver
// Observer Pattern: Concrete Subject
public class OrderManager implements OrderSubject {
    private final Map<String, OrderEntry> orders;
    private final List<OrderObserver> observers;
    private int orderCounter;

    public enum OrderStatus {
        PLACED("Order Placed"),
        PREPARING("Preparing"),
        READY("Ready for Pickup"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");

        private final String displayName;

        OrderStatus(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public static class OrderEntry {
        private final OrderComponent order;
        private OrderStatus status;

        public OrderEntry(OrderComponent order, OrderStatus status) {
            this.order = order;
            this.status = status;
        }

        public OrderComponent getOrder() {
            return order;
        }

        public OrderStatus getStatus() {
            return status;
        }

        public void setStatus(OrderStatus status) {
            this.status = status;
        }
    }

    public OrderManager() {
        this.orders = new HashMap<>();
        this.observers = new ArrayList<>();
        this.orderCounter = 1000;
    }

    // Order management methods
    public String addOrder(OrderComponent order) {
        String orderId = generateOrderId();
        orders.put(orderId, new OrderEntry(order, OrderStatus.PLACED));
        notifyOrderPlaced(orderId);
        return orderId;
    }

    public void removeOrder(String orderId) {
        if (orders.containsKey(orderId)) {
            orders.remove(orderId);
            notifyOrderCancelled(orderId);
        }
    }

    public void cancelOrder(String orderId) {
        OrderEntry entry = orders.get(orderId);
        if (entry != null) {
            entry.setStatus(OrderStatus.CANCELLED);
            notifyOrderCancelled(orderId);
        }
    }

    public void restoreOrder(String orderId, OrderEntry entry) {
        entry.setStatus(OrderStatus.PLACED);
        orders.put(orderId, entry);
        notifyOrderPlaced(orderId);
    }

    public void updateOrderStatus(String orderId, OrderStatus status) {
        OrderEntry entry = orders.get(orderId);
        if (entry != null) {
            entry.setStatus(status);
            switch (status) {
                case PREPARING:
                    notifyOrderPreparing(orderId);
                    break;
                case READY:
                    notifyOrderReady(orderId);
                    break;
                case CANCELLED:
                    notifyOrderCancelled(orderId);
                    break;
                default:
                    break;
            }
        }
    }

    public OrderStatus getOrderStatus(String orderId) {
        OrderEntry entry = orders.get(orderId);
        return entry != null ? entry.getStatus() : null;
    }

    public OrderEntry getOrderEntry(String orderId) {
        return orders.get(orderId);
    }

    public OrderComponent getOrder(String orderId) {
        OrderEntry entry = orders.get(orderId);
        return entry != null ? entry.getOrder() : null;
    }

    public List<String> getAllOrderIds() {
        return new ArrayList<>(orders.keySet());
    }

    public void printAllOrders() {
        System.out.println("\n=== All Orders ===");
        if (orders.isEmpty()) {
            System.out.println("No orders currently.");
        } else {
            for (Map.Entry<String, OrderEntry> entry : orders.entrySet()) {
                System.out.println("Order #" + entry.getKey() + 
                    " - Status: " + entry.getValue().getStatus() +
                    " - " + entry.getValue().getOrder().getDescription());
            }
        }
        System.out.println("==================\n");
    }

    private String generateOrderId() {
        return String.valueOf(++orderCounter);
    }

    // Observer Pattern implementation
    @Override
    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyOrderPlaced(String orderId) {
        OrderEntry entry = orders.get(orderId);
        if (entry != null) {
            for (OrderObserver observer : observers) {
                observer.onOrderPlaced(entry.getOrder(), orderId);
            }
        }
    }

    @Override
    public void notifyOrderPreparing(String orderId) {
        for (OrderObserver observer : observers) {
            observer.onOrderPreparing(orderId);
        }
    }

    @Override
    public void notifyOrderReady(String orderId) {
        for (OrderObserver observer : observers) {
            observer.onOrderReady(orderId);
        }
    }

    @Override
    public void notifyOrderCancelled(String orderId) {
        for (OrderObserver observer : observers) {
            observer.onOrderCancelled(orderId);
        }
    }
}
