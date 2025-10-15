import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

// Demonstrates OCP: new pricing modifiers can be registered without changing this class.
public class PriceCalculator {
    private final Map<String, Double> basePrices;
    private final List<BiFunction<CoffeeOrder, Double, Double>> modifiers;

    public PriceCalculator(Map<String, Double> basePrices, List<BiFunction<CoffeeOrder, Double, Double>> modifiers) {
        this.basePrices = basePrices;
        this.modifiers = new ArrayList<>();
        if (modifiers != null) {
            this.modifiers.addAll(modifiers);
        }
    }

    public void addModifier(BiFunction<CoffeeOrder, Double, Double> modifier) {
        modifiers.add(modifier);
    }

    public double calculate(CoffeeOrder order) {
        Double startingPrice = basePrices.get(order.getBeverage());
        if (startingPrice == null) {
            throw new IllegalArgumentException("Unknown beverage: " + order.getBeverage());
        }
        double total = startingPrice;
        for (BiFunction<CoffeeOrder, Double, Double> modifier : modifiers) {
            total = modifier.apply(order, total);
        }
        return Math.round(total * 100.0) / 100.0;
    }

    public static double applySizeModifier(CoffeeOrder order, Double runningTotal) {
        Map<String, Double> sizeAdjustments = new HashMap<>();
        sizeAdjustments.put("small", 0.0);
        sizeAdjustments.put("medium", 0.5);
        sizeAdjustments.put("large", 1.0);
        
        Double adjustment = sizeAdjustments.get(order.getSize());
        if (adjustment == null) {
            throw new IllegalArgumentException("Unknown size: " + order.getSize());
        }
        return runningTotal + adjustment;
    }

    public static double applyExtrasModifier(CoffeeOrder order, Double runningTotal) {
        Map<String, Double> extrasPrices = new HashMap<>();
        extrasPrices.put("soy", 0.4);
        extrasPrices.put("oat", 0.5);
        extrasPrices.put("vanilla", 0.6);
        extrasPrices.put("caramel", 0.7);
        
        double extrasTotal = 0.0;
        for (String extra : order.getExtras()) {
            Double price = extrasPrices.get(extra);
            if (price != null) {
                extrasTotal += price;
            }
        }
        return runningTotal + extrasTotal;
    }
}
