package lab2.strategy;

// Strategy Pattern: Concrete Strategy
public class LoyaltyPointsPaymentStrategy implements PaymentStrategy {
    private final String customerId;
    private int availablePoints;
    private static final int POINTS_PER_DOLLAR = 100; // 100 points = $1

    public LoyaltyPointsPaymentStrategy(String customerId, int availablePoints) {
        this.customerId = customerId;
        this.availablePoints = availablePoints;
    }

    @Override
    public boolean pay(double amount) {
        int requiredPoints = (int) (amount * POINTS_PER_DOLLAR);
        
        System.out.println("[PAYMENT] Processing loyalty points payment...");
        System.out.println("[PAYMENT] Customer ID: " + customerId);
        System.out.println("[PAYMENT] Available points: " + availablePoints);
        System.out.println("[PAYMENT] Required points: " + requiredPoints);
        
        if (availablePoints >= requiredPoints) {
            availablePoints -= requiredPoints;
            System.out.println("[PAYMENT] Points redeemed successfully!");
            System.out.println("[PAYMENT] Remaining points: " + availablePoints);
            return true;
        } else {
            int shortfall = requiredPoints - availablePoints;
            System.out.println("[PAYMENT] Insufficient points. Need " + shortfall + " more points.");
            return false;
        }
    }

    @Override
    public String getPaymentMethod() {
        return "Loyalty Points";
    }

    public int getAvailablePoints() {
        return availablePoints;
    }

    public void addPoints(int points) {
        this.availablePoints += points;
    }
}
