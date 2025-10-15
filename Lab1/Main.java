import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        Map<String, Double> basePrices = new HashMap<>();
        basePrices.put("espresso", 2.5);
        basePrices.put("latte", 3.5);
        basePrices.put("cappuccino", 3.8);

        PriceCalculator calculator = new PriceCalculator(
                basePrices,
                Arrays.asList(
                        PriceCalculator::applySizeModifier,
                        PriceCalculator::applyExtrasModifier
                )
        );

        List<String> storedOrders = new ArrayList<>();

        BiConsumer<CoffeeOrder, Double> saveToMemory = (order, total) ->
                storedOrders.add(order.describe() + " Total: $" + String.format("%.2f", total));

        Consumer<String> notifyConsole = System.out::println;

        CoffeeService service = new CoffeeService(calculator, saveToMemory, notifyConsole);

        Scanner scanner = new Scanner(System.in);

        System.out.print("Choose beverage (espresso/latte/cappuccino): ");
        String beverage = scanner.nextLine().trim().toLowerCase();

        System.out.print("Choose size (small/medium/large): ");
        String size = scanner.nextLine().trim().toLowerCase();

        System.out.print("Extras (comma separated, blank for none): ");
        String extrasInput = scanner.nextLine();

        List<String> extras = new ArrayList<>();
        if (extrasInput != null && !extrasInput.trim().isEmpty()) {
            String[] parts = extrasInput.split(",");
            for (String part : parts) {
                String extra = part.trim().toLowerCase();
                if (!extra.isEmpty()) {
                    extras.add(extra);
                }
            }
        }

        CoffeeOrder order = new CoffeeOrder(beverage, size, extras);
        double total = service.process(order);

        System.out.println("Stored " + storedOrders.size() + " order(s). Last total: $" + String.format("%.2f", total));

        scanner.close();
    }
}
