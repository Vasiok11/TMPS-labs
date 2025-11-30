package lab2.observer;

import lab2.composite.OrderComponent;

// Observer Pattern: Concrete Observer
public class LoyaltyPointsObserver implements OrderObserver {
    private int totalPoints;
    private static final int POINTS_PER_DOLLAR = 10;

    public LoyaltyPointsObserver() {
        this.totalPoints = 0;
    }

    @Override
    public void onOrderPlaced(OrderComponent order, String orderId) {
        // Points are calculated but not added until order is ready
        int potentialPoints = (int) (order.getTotalCost() * POINTS_PER_DOLLAR);
        System.out.println("[LOYALTY] You will earn " + potentialPoints + " points for order #" + orderId);
    }

    @Override
    public void onOrderPreparing(String orderId) {
        // No action needed
    }

    @Override
    public void onOrderReady(String orderId) {
        int earnedPoints = 50;
        totalPoints += earnedPoints;
        System.out.println("[LOYALTY] You earned " + earnedPoints + " points! Total: " + totalPoints + " points");
    }

    @Override
    public void onOrderCancelled(String orderId) {
        System.out.println("[LOYALTY] No points awarded for cancelled order #" + orderId);
    }

    public int getTotalPoints() {
        return totalPoints;
    }
}
