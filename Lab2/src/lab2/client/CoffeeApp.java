package lab2.client;

import java.util.Locale;
import java.util.Scanner;

import lab2.domain.CoffeeOrderRequest;
import lab2.domain.OrderService;
import lab2.models.Coffee;
import lab2.models.CoffeeType;
import lab2.models.Size;

public final class CoffeeApp {
    private CoffeeApp() {
    }

    public static void main(String[] args) {
        OrderService orderService = new OrderService();

        try (Scanner scanner = new Scanner(System.in)) {
            scanner.useLocale(Locale.US);
            boolean running = true;
            while (running) {
                printMenu();
                int choice = readChoice(scanner);
                if (choice == 0) {
                    running = false;
                    continue;
                }

                CoffeeType type = resolveType(choice);
                if (type == null) {
                    System.out.println("Please choose a valid option.");
                    continue;
                }

                CoffeeOrderRequest request = gatherCustomization(scanner);
                Coffee coffee = orderService.placeOrder(type, request);
                System.out.println("\nPrepared: " + coffee + '\n');
            }
        }

        System.out.println("Goodbye!");
    }

    private static void printMenu() {
        System.out.println("=== Coffee Shop Menu ===");
        System.out.println("1. Espresso");
        System.out.println("2. Latte");
        System.out.println("3. Cappuccino");
        System.out.println("0. Exit");
        System.out.print("Select an option: ");
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
