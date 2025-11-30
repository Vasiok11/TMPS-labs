package lab2.strategy;

// Strategy Pattern: Concrete Strategy
public class CreditCardPaymentStrategy implements PaymentStrategy {
    private final String cardNumber;
    private final String cardHolderName;
    private final String cvv;
    private final String expiryDate;

    public CreditCardPaymentStrategy(String cardNumber, String cardHolderName, 
                                      String cvv, String expiryDate) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean pay(double amount) {
        // Simulate card validation
        if (validateCard()) {
            System.out.println("[PAYMENT] Processing credit card payment...");
            System.out.println("[PAYMENT] Card: **** **** **** " + cardNumber.substring(cardNumber.length() - 4));
            System.out.println("[PAYMENT] Amount: $" + String.format("%.2f", amount));
            System.out.println("[PAYMENT] Credit card payment successful!");
            return true;
        } else {
            System.out.println("[PAYMENT] Credit card validation failed.");
            return false;
        }
    }

    private boolean validateCard() {
        return cardNumber != null && cardNumber.length() >= 13 
            && cvv != null && cvv.length() >= 3;
    }

    @Override
    public String getPaymentMethod() {
        return "Credit Card";
    }
}
