package lab2.client;

import java.util.Locale;
import java.util.Scanner;

import lab2.command.OrderManager;
import lab2.composite.ComboOrder;
import lab2.composite.OrderComponent;
import lab2.domain.CoffeeOrderRequest;
import lab2.facade.CoffeeShopFacade;
import lab2.models.CoffeeType;
import lab2.models.Size;

// Behavioral Design Patterns: Observer, Command, Strategy
public final class CoffeeApp {
    private static final CoffeeShopFacade facade = new CoffeeShopFacade();
    private static String lastOrderId = null;

    private CoffeeApp() {
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            scanner.useLocale(Locale.US);
            
            System.out.print("Welcome! Please enter your name: ");
            String customerName = scanner.nextLine().trim();
            if (customerName.isEmpty()) {
                customerName = "Customer";
            }
            facade.setCustomerName(customerName);
            System.out.println("Hello, " + customerName + "! Let's get you some coffee.\n");
            
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
                    case 5:
                        manageOrders(scanner);
                        break;
                    case 6:
                        processPayment(scanner);
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
        System.out.println("5. Manage Orders (Command Pattern)");
        System.out.println("6. Process Payment (Strategy Pattern)");
        System.out.println("0. Exit");
        System.out.print("Select an option: ");
    }

    private static void orderSingleCoffee(Scanner scanner) {
        CoffeeType type = selectCoffeeType(scanner);
        if (type == null) return;

        CoffeeOrderRequest request = gatherCustomization(scanner);
        OrderComponent order = facade.orderSingleCoffee(type, request);
        facade.printOrderSummary(order);
        
        System.out.println("\n--- Placing Order (Command Pattern) ---");
        lastOrderId = facade.placeOrderWithCommand(order);
    }

    private static void orderDecoratedCoffee(Scanner scanner) {
        CoffeeType type = selectCoffeeType(scanner);
        if (type == null) return;

        CoffeeOrderRequest request = gatherCustomization(scanner);

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
        
        System.out.println("\n--- Placing Order (Command Pattern) ---");
        lastOrderId = facade.placeOrderWithCommand(order);
    }

    private static void orderPopularCoffee(Scanner scanner) {
        CoffeeType type = selectCoffeeType(scanner);
        if (type == null) return;

        CoffeeOrderRequest request = gatherCustomization(scanner);
        OrderComponent order = facade.orderPopularCoffee(type, request);
        System.out.println("\n(Popular preset decorations applied for " + type.getDisplayName() + ")");
        facade.printOrderSummary(order);
        
        System.out.println("\n--- Placing Order (Command Pattern) ---");
        lastOrderId = facade.placeOrderWithCommand(order);
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
        
        System.out.println("\n--- Placing Combo Order (Command Pattern) ---");
        lastOrderId = facade.placeOrderWithCommand(combo);
    }
    
    private static void manageOrders(Scanner scanner) {
        boolean managing = true;
        while (managing) {
            System.out.println("\n=== Order Management (Command Pattern) ===");
            System.out.println("1. View All Orders");
            System.out.println("2. Update Order Status");
            System.out.println("3. Cancel Order");
            System.out.println("4. Undo Last Action");
            System.out.println("5. Redo Last Action");
            System.out.println("6. View Command History");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select: ");
            
            int choice = readChoice(scanner);
            switch (choice) {
                case 0:
                    managing = false;
                    break;
                case 1:
                    facade.printAllOrders();
                    break;
                case 2:
                    updateOrderStatus(scanner);
                    break;
                case 3:
                    cancelOrder(scanner);
                    break;
                case 4:
                    System.out.println("\n--- Undo (Command Pattern) ---");
                    facade.undoLastCommand();
                    break;
                case 5:
                    System.out.println("\n--- Redo (Command Pattern) ---");
                    facade.redoLastCommand();
                    break;
                case 6:
                    facade.printCommandHistory();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    private static void updateOrderStatus(Scanner scanner) {
        System.out.print("Enter Order ID (or press Enter for last order): ");
        String orderId = scanner.nextLine().trim();
        if (orderId.isEmpty() && lastOrderId != null) {
            orderId = lastOrderId;
        }
        if (orderId.isEmpty()) {
            System.out.println("No order ID specified.");
            return;
        }
        
        System.out.println("Select new status:");
        System.out.println("1. Preparing");
        System.out.println("2. Ready");
        System.out.println("3. Completed");
        System.out.print("Choice: ");
        
        int choice = readChoice(scanner);
        OrderManager.OrderStatus status;
        switch (choice) {
            case 1:
                status = OrderManager.OrderStatus.PREPARING;
                break;
            case 2:
                status = OrderManager.OrderStatus.READY;
                break;
            case 3:
                status = OrderManager.OrderStatus.COMPLETED;
                break;
            default:
                System.out.println("Invalid status.");
                return;
        }
        
        facade.updateOrderStatus(orderId, status);
    }
    
    private static void cancelOrder(Scanner scanner) {
        System.out.print("Enter Order ID to cancel (or press Enter for last order): ");
        String orderId = scanner.nextLine().trim();
        if (orderId.isEmpty() && lastOrderId != null) {
            orderId = lastOrderId;
        }
        if (orderId.isEmpty()) {
            System.out.println("No order ID specified.");
            return;
        }
        
        facade.cancelOrder(orderId);
    }
    
    private static void processPayment(Scanner scanner) {
        if (lastOrderId == null) {
            System.out.println("No order to pay for. Please place an order first.");
            return;
        }
        
        System.out.println("\n=== Payment (Strategy Pattern) ===");
        System.out.println("Select payment method:");
        System.out.println("1. Cash");
        System.out.println("2. Credit Card");
        System.out.println("3. Mobile Payment (Apple Pay / Google Pay)");
        System.out.println("4. Loyalty Points");
        System.out.print("Choice: ");
        
        int choice = readChoice(scanner);
        
        // Get order total (we'll use a sample amount)
        System.out.print("Enter amount to pay: $");
        String amountStr = scanner.nextLine().trim();
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            amount = 5.00; // Default
            System.out.println("Using default amount: $5.00");
        }
        
        switch (choice) {
            case 1:
                System.out.print("Enter cash amount: $");
                String cashStr = scanner.nextLine().trim();
                double cashAmount;
                try {
                    cashAmount = Double.parseDouble(cashStr);
                } catch (NumberFormatException e) {
                    cashAmount = amount;
                }
                facade.setPaymentCash(cashAmount);
                break;
            case 2:
                System.out.print("Enter card number: ");
                String cardNumber = scanner.nextLine().trim();
                if (cardNumber.isEmpty()) cardNumber = "4111111111111111";
                System.out.print("Enter cardholder name: ");
                String holderName = scanner.nextLine().trim();
                if (holderName.isEmpty()) holderName = "John Doe";
                System.out.print("Enter CVV: ");
                String cvv = scanner.nextLine().trim();
                if (cvv.isEmpty()) cvv = "123";
                System.out.print("Enter expiry (MM/YY): ");
                String expiry = scanner.nextLine().trim();
                if (expiry.isEmpty()) expiry = "12/25";
                facade.setPaymentCreditCard(cardNumber, holderName, cvv, expiry);
                break;
            case 3:
                System.out.print("Enter phone number: ");
                String phone = scanner.nextLine().trim();
                if (phone.isEmpty()) phone = "555-1234";
                System.out.print("Payment app (1=Apple Pay, 2=Google Pay): ");
                int appChoice = readChoice(scanner);
                String app = appChoice == 2 ? "Google Pay" : "Apple Pay";
                facade.setPaymentMobile(phone, app);
                break;
            case 4:
                System.out.print("Enter customer ID: ");
                String customerId = scanner.nextLine().trim();
                if (customerId.isEmpty()) customerId = "CUST001";
                System.out.print("Enter available points: ");
                String pointsStr = scanner.nextLine().trim();
                int points;
                try {
                    points = Integer.parseInt(pointsStr);
                } catch (NumberFormatException e) {
                    points = 1000;
                }
                facade.setPaymentLoyaltyPoints(customerId, points);
                break;
            default:
                System.out.println("Invalid choice. Using cash.");
                facade.setPaymentCash(amount);
        }
        
        boolean success = facade.processPayment(amount);
        if (success) {
            System.out.println("Payment successful! Thank you for your order.");
            facade.updateOrderStatus(lastOrderId, OrderManager.OrderStatus.PREPARING);
        } else {
            System.out.println("Payment failed. Please try again.");
        }
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
