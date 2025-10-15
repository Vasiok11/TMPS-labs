import java.util.List;
import java.util.stream.Collectors;

// Demonstrates SRP: this class is only responsible for representing a coffee order.
public class CoffeeOrder {
    private final String beverage;
    private final String size;
    private final List<String> extras;

    public CoffeeOrder(String beverage, String size, List<String> extras) {
        this.beverage = beverage;
        this.size = size;
        this.extras = extras;
    }

    public String getBeverage() {
        return beverage;
    }

    public String getSize() {
        return size;
    }

    public List<String> getExtras() {
        return extras;
    }

    public String describe() {
        if (extras == null || extras.isEmpty()) {
            return capitalize(size) + " " + capitalize(beverage) + " with no extras.";
        }
        String extrasPart = extras.stream()
                .map(this::capitalize)
                .collect(Collectors.joining(", "));
        return capitalize(size) + " " + capitalize(beverage) + " with " + extrasPart + ".";
    }

    private String capitalize(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }
}
