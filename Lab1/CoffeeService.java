import java.util.function.BiConsumer;
import java.util.function.Consumer;

// Demonstrates DIP: depends on abstractions (functional interfaces) instead of concrete implementations.
public class CoffeeService {
    private final PriceCalculator calculator;
    private final BiConsumer<CoffeeOrder, Double> saveOrder;
    private final Consumer<String> notify;

    public CoffeeService(PriceCalculator calculator, BiConsumer<CoffeeOrder, Double> saveOrder, Consumer<String> notify) {
        this.calculator = calculator;
        this.saveOrder = saveOrder;
        this.notify = notify;
    }

    public double process(CoffeeOrder order) {
        double total = calculator.calculate(order);
        saveOrder.accept(order, total);
        notify.accept(String.format("Order ready: %s Total: $%.2f", order.describe(), total));
        return total;
    }
}
