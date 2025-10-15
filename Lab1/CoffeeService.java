import java.util.function.BiConsumer;
import java.util.function.Consumer;

// Demonstrates DIP: depends on abstractions (IPriceCalculator, IOrder interfaces) instead of concrete implementations.
public class CoffeeService {
    private final IPriceCalculator calculator;
    private final BiConsumer<IOrder, Double> saveOrder;
    private final Consumer<String> notify;

    public CoffeeService(IPriceCalculator calculator, BiConsumer<IOrder, Double> saveOrder, Consumer<String> notify) {
        this.calculator = calculator;
        this.saveOrder = saveOrder;
        this.notify = notify;
    }

    public double process(IOrder order) {
        double total = calculator.calculate(order);
        saveOrder.accept(order, total);
        notify.accept(String.format("Order ready: %s Total: $%.2f", order.describe(), total));
        return total;
    }
}
