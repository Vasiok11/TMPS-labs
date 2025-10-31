package lab2.models;

public enum CoffeeType {
    ESPRESSO("Espresso"),
    LATTE("Latte"),
    CAPPUCCINO("Cappuccino");

    private final String displayName;

    CoffeeType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
