package lab2.strategy;

// Strategy Pattern: Concrete Strategy
public class MobilePaymentStrategy implements PaymentStrategy {
    private final String phoneNumber;
    private final String paymentApp; 

    public MobilePaymentStrategy(String phoneNumber, String paymentApp) {
        this.phoneNumber = phoneNumber;
        this.paymentApp = paymentApp;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("[PAYMENT] Initiating " + paymentApp + " payment...");
        System.out.println("[PAYMENT] Sending payment request to " + maskPhoneNumber() + "...");
        
        // Simulate NFC/QR payment
        if (simulatePaymentConfirmation()) {
            System.out.println("[PAYMENT] Amount: $" + String.format("%.2f", amount));
            System.out.println("[PAYMENT] " + paymentApp + " payment successful! ✓");
            return true;
        } else {
            System.out.println("[PAYMENT] " + paymentApp + " payment failed. Please try again.");
            return false;
        }
    }

    private String maskPhoneNumber() {
        if (phoneNumber.length() >= 4) {
            return "***-***-" + phoneNumber.substring(phoneNumber.length() - 4);
        }
        return phoneNumber;
    }

    private boolean simulatePaymentConfirmation() {
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return paymentApp;
    }
}
