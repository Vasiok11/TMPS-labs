package lab2.strategy;

// Strategy Pattern: Concrete Strategy
public class CashPaymentStrategy implements PaymentStrategy {
    private double cashReceived;

    public CashPaymentStrategy(double cashReceived) {
        this.cashReceived = cashReceived;
    }

    @Override
    public boolean pay(double amount) {
        if (cashReceived >= amount) {
            double change = cashReceived - amount;
            System.out.println("[PAYMENT] Processing cash payment...");
            System.out.println("[PAYMENT] Amount due: $" + String.format("%.2f", amount));
            System.out.println("[PAYMENT] Cash received: $" + String.format("%.2f", cashReceived));
            if (change > 0) {
                System.out.println("[PAYMENT] Change: $" + String.format("%.2f", change));
            }
            System.out.println("[PAYMENT] Cash payment successful!");
            return true;
        } else {
            System.out.println("[PAYMENT] Insufficient cash. Need $" + 
                String.format("%.2f", amount - cashReceived) + " more.");
            return false;
        }
    }

    @Override
    public String getPaymentMethod() {
        return "Cash";
    }

    public void setCashReceived(double cashReceived) {
        this.cashReceived = cashReceived;
    }
}
