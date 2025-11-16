package lab2.client;

import java.util.Locale;
import java.util.Scanner;

import lab2.composite.ComboOrder;
import lab2.composite.OrderComponent;
import lab2.domain.CoffeeOrderRequest;
import lab2.facade.CoffeeShopFacade;
import lab2.models.CoffeeType;
import lab2.models.Size;

public final class CoffeeApp {
    // Single unified client using Facade Pattern
    private static final CoffeeShopFacade facade = new CoffeeShopFacade();

    private CoffeeApp() {
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            scanner.useLocale(Locale.US);
            boolean running = true;
            while (running) {
                printMainMenu();
                int choice = readChoice(scanner);
                
                switch (choice) {
                    case 0:
                        running = false;
                        break;
                    case 1:
                        orderSingleCoffee(scanner);
                        break;
                    case 2:
                        orderDecoratedCoffee(scanner);
                        break;
                    case 3:
                        orderPopularCoffee(scanner);
                        break;
                    case 4:
                        orderCombo(scanner);
                        break;
                    default:
                        System.out.println("Please choose a valid option.");
                }
            }
        }

        System.out.println("Goodbye!");
    }

    private static void printMainMenu() {
        System.out.println("\n=== Coffee Shop Menu ===");
        System.out.println("1. Order Single Coffee");
        System.out.println("2. Order Decorated Coffee (with toppings)");
        System.out.println("3. Order Popular Coffee (preset decorations)");
        System.out.println("4. Order Combo Deal");
        System.out.println("0. Exit");
        System.out.print("Select an option: ");
    }

    private static void orderSingleCoffee(Scanner scanner) {
        CoffeeType type = selectCoffeeType(scanner);
        if (type == null) return;

        CoffeeOrderRequest request = gatherCustomization(scanner);
        OrderComponent order = facade.orderSingleCoffee(type, request);
        facade.printOrderSummary(order);
    }

    private static void orderDecoratedCoffee(Scanner scanner) {
        CoffeeType type = selectCoffeeType(scanner);
        if (type == null) return;

        CoffeeOrderRequest request = gatherCustomization(scanner);

        // Gather decorations
        System.out.print("Add whipped cream? (y/N): ");
        boolean whippedCream = scanner.nextLine().trim().toLowerCase().startsWith("y");

        System.out.print("Add extra shot? (y/N): ");
        boolean extraShot = scanner.nextLine().trim().toLowerCase().startsWith("y");

        System.out.print("Add flavor syrup? (Vanilla/Hazelnut/Caramel/none): ");
        String syrup = scanner.nextLine().trim();
        if (syrup.equalsIgnoreCase("none")) {
            syrup = null;
        }

        System.out.print("Add caramel drizzle? (y/N): ");
        boolean caramel = scanner.nextLine().trim().toLowerCase().startsWith("y");

        OrderComponent order = facade.orderDecoratedCoffee(
            type, request, whippedCream, extraShot, syrup, caramel
        );
        facade.printOrderSummary(order);
    }

    private static void orderPopularCoffee(Scanner scanner) {
        CoffeeType type = selectCoffeeType(scanner);
        if (type == null) return;

        CoffeeOrderRequest request = gatherCustomization(scanner);
        OrderComponent order = facade.orderPopularCoffee(type, request);
        System.out.println("\n(Popular preset decorations applied for " + type.getDisplayName() + ")");
        facade.printOrderSummary(order);
    }

    private static void orderCombo(Scanner scanner) {
        System.out.print("Enter combo name: ");
        String comboName = scanner.nextLine().trim();
        if (comboName.isEmpty()) {
            comboName = "Special Combo";
        }

        System.out.print("Enter discount percentage (0-50): ");
        String discountStr = scanner.nextLine().trim();
        double discount = 0.0;
        try {
            discount = Double.parseDouble(discountStr) / 100.0;
            if (discount < 0 || discount > 0.5) {
                discount = 0.1; // Default 10%
            }
        } catch (NumberFormatException e) {
            discount = 0.1; // Default 10%
        }

        ComboOrder combo = facade.createComboOrder(comboName, discount);

        System.out.print("How many items in combo? (1-5): ");
        int itemCount = readChoice(scanner);
        if (itemCount < 1 || itemCount > 5) {
            itemCount = 2;
        }

        for (int i = 1; i <= itemCount; i++) {
            System.out.println("\n--- Item " + i + " ---");
            CoffeeType type = selectCoffeeType(scanner);
            if (type == null) continue;

            CoffeeOrderRequest request = gatherCustomization(scanner);
            OrderComponent order = facade.orderSingleCoffee(type, request);
            combo.addOrder(order);
        }

        facade.printOrderSummary(combo);
    }

    private static CoffeeType selectCoffeeType(Scanner scanner) {
        System.out.println("\nSelect Coffee Type:");
        System.out.println("1. Espresso");
        System.out.println("2. Latte");
        System.out.println("3. Cappuccino");
        System.out.print("Choice: ");
        
        int choice = readChoice(scanner);
        return resolveType(choice);
    }

    private static int readChoice(Scanner scanner) {
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            return -1;
        }
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private static CoffeeOrderRequest gatherCustomization(Scanner scanner) {
        CoffeeOrderRequest.Builder builder = CoffeeOrderRequest.builder();

        System.out.print("Preferred size (S/M/L, blank for default): ");
        String sizeInput = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
        if (!sizeInput.isEmpty()) {
            Size size = parseSize(sizeInput);
            if (size != null) {
                builder.size(size);
            } else {
                System.out.println("Unknown size, keeping recipe default.");
            }
        }

        System.out.print("Milk choice (blank for recipe default): ");
        String milk = scanner.nextLine().trim();
        if (!milk.isEmpty()) {
            builder.milkType(milk);
        }

        System.out.print("Extra add-ons separated by comma (blank for none): ");
        String addOnsLine = scanner.nextLine();
        if (!addOnsLine.isBlank()) {
            String[] tokens = addOnsLine.split(",");
            for (String token : tokens) {
                String addOn = token.trim();
                if (!addOn.isEmpty()) {
                    builder.addOn(addOn);
                }
            }
        }

        System.out.print("Takeaway? (y/N): ");
        String takeawayInput = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
        if (!takeawayInput.isEmpty() && takeawayInput.charAt(0) == 'y') {
            builder.takeaway(true);
        }

        return builder.build();
    }

    private static Size parseSize(String input) {
        switch (input.charAt(0)) {
            case 'S':
                return Size.SMALL;
            case 'M':
                return Size.MEDIUM;
            case 'L':
                return Size.LARGE;
            default:
                return null;
        }
    }

    private static CoffeeType resolveType(int choice) {
        switch (choice) {
            case 1:
                return CoffeeType.ESPRESSO;
            case 2:
                return CoffeeType.LATTE;
            case 3:
                return CoffeeType.CAPPUCCINO;
            default:
                return null;
        }
    }
}
