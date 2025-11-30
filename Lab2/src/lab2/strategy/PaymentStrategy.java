package lab2.strategy;

// Strategy Pattern: Strategy Interface
public interface PaymentStrategy {
    boolean pay(double amount);
    String getPaymentMethod();
}
