package lab2.strategy;

// Strategy Pattern: Context
public class PaymentProcessor {
    private PaymentStrategy paymentStrategy;

    public PaymentProcessor() {
        // Default to cash payment
        this.paymentStrategy = new CashPaymentStrategy(0);
    }

    public PaymentProcessor(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public boolean processPayment(double amount) {
        if (paymentStrategy == null) {
            System.out.println("[PAYMENT] No payment method selected!");
            return false;
        }
        
        System.out.println("\n--- Processing Payment ---");
        System.out.println("Payment Method: " + paymentStrategy.getPaymentMethod());
        boolean result = paymentStrategy.pay(amount);
        System.out.println("--------------------------\n");
        
        return result;
    }

    public String getCurrentPaymentMethod() {
        return paymentStrategy != null ? paymentStrategy.getPaymentMethod() : "None";
    }
}
