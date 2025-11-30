package lab2.facade;

import lab2.command.CancelOrderCommand;
import lab2.command.OrderCommand;
import lab2.command.OrderCommandInvoker;
import lab2.command.OrderManager;
import lab2.command.PlaceOrderCommand;
import lab2.command.UpdateOrderStatusCommand;
import lab2.composite.ComboOrder;
import lab2.composite.OrderComponent;
import lab2.composite.SingleOrder;
import lab2.decorator.BaseCoffeeComponent;
import lab2.decorator.CaramelDrizzleDecorator;
import lab2.decorator.CoffeeComponent;
import lab2.decorator.ExtraShotDecorator;
import lab2.decorator.FlavorSyrupDecorator;
import lab2.decorator.WhippedCreamDecorator;
import lab2.domain.CoffeeOrderRequest;
import lab2.domain.OrderService;
import lab2.models.Coffee;
import lab2.models.CoffeeType;
import lab2.observer.CustomerNotificationObserver;
import lab2.observer.KitchenDisplayObserver;
import lab2.observer.LoyaltyPointsObserver;
import lab2.observer.OrderObserver;
import lab2.strategy.CashPaymentStrategy;
import lab2.strategy.CreditCardPaymentStrategy;
import lab2.strategy.LoyaltyPointsPaymentStrategy;
import lab2.strategy.MobilePaymentStrategy;
import lab2.strategy.PaymentProcessor;

// Facade Pattern + Behavioral Patterns: Observer, Command, Strategy
public class CoffeeShopFacade {
    private final OrderService orderService;
    
    private final OrderManager orderManager;           // Observer Pattern (Subject)
    private final OrderCommandInvoker commandInvoker;  // Command Pattern (Invoker)
    private final PaymentProcessor paymentProcessor;   // Strategy Pattern (Context)
    
    private CustomerNotificationObserver customerObserver;
    private final KitchenDisplayObserver kitchenObserver;
    private final LoyaltyPointsObserver loyaltyObserver;

    public CoffeeShopFacade() {
        this.orderService = new OrderService();
        
        this.orderManager = new OrderManager();
        this.commandInvoker = new OrderCommandInvoker();
        this.paymentProcessor = new PaymentProcessor();
        
        this.kitchenObserver = new KitchenDisplayObserver();
        this.loyaltyObserver = new LoyaltyPointsObserver();
        
        orderManager.addObserver(kitchenObserver);
        orderManager.addObserver(loyaltyObserver);
    }
    
    public void setCustomerName(String customerName) {
        if (customerObserver != null) {
            orderManager.removeObserver(customerObserver);
        }
        customerObserver = new CustomerNotificationObserver(customerName);
        orderManager.addObserver(customerObserver);
    }
    
    public void addObserver(OrderObserver observer) {
        orderManager.addObserver(observer);
    }
    
    public void removeObserver(OrderObserver observer) {
        orderManager.removeObserver(observer);
    }
    
    public String placeOrderWithCommand(OrderComponent order) {
        PlaceOrderCommand command = new PlaceOrderCommand(orderManager, order);
        commandInvoker.executeCommand(command);
        return command.getOrderId();
    }
    
    public void cancelOrder(String orderId) {
        OrderCommand command = new CancelOrderCommand(orderManager, orderId);
        commandInvoker.executeCommand(command);
    }
    
    public void updateOrderStatus(String orderId, OrderManager.OrderStatus status) {
        OrderCommand command = new UpdateOrderStatusCommand(orderManager, orderId, status);
        commandInvoker.executeCommand(command);
    }
    
    public void undoLastCommand() {
        commandInvoker.undoLastCommand();
    }
    
    public void redoLastCommand() {
        commandInvoker.redoLastCommand();
    }
    
    public boolean canUndo() {
        return commandInvoker.canUndo();
    }
    
    public boolean canRedo() {
        return commandInvoker.canRedo();
    }
    
    public void printCommandHistory() {
        commandInvoker.printCommandHistory();
    }
    
    public void printAllOrders() {
        orderManager.printAllOrders();
    }
    
    public void setPaymentCash(double cashAmount) {
        paymentProcessor.setPaymentStrategy(new CashPaymentStrategy(cashAmount));
    }
    
    public void setPaymentCreditCard(String cardNumber, String holderName, String cvv, String expiry) {
        paymentProcessor.setPaymentStrategy(
            new CreditCardPaymentStrategy(cardNumber, holderName, cvv, expiry)
        );
    }
    
    public void setPaymentMobile(String phoneNumber, String paymentApp) {
        paymentProcessor.setPaymentStrategy(new MobilePaymentStrategy(phoneNumber, paymentApp));
    }
    
    public void setPaymentLoyaltyPoints(String customerId, int points) {
        paymentProcessor.setPaymentStrategy(new LoyaltyPointsPaymentStrategy(customerId, points));
    }
    
    public boolean processPayment(double amount) {
        return paymentProcessor.processPayment(amount);
    }
    
    public String getCurrentPaymentMethod() {
        return paymentProcessor.getCurrentPaymentMethod();
    }

    public OrderComponent orderSingleCoffee(CoffeeType type, CoffeeOrderRequest request) {
        Coffee coffee = orderService.placeOrder(type, request);
        CoffeeComponent component = new BaseCoffeeComponent(coffee);
        return new SingleOrder(component);
    }

    public OrderComponent orderDecoratedCoffee(
            CoffeeType type,
            CoffeeOrderRequest request,
            boolean addWhippedCream,
            boolean addExtraShot,
            String syrupFlavor,
            boolean addCaramel) {
        
        Coffee coffee = orderService.placeOrder(type, request);
        CoffeeComponent component = new BaseCoffeeComponent(coffee);

        // Apply decorators based on preferences
        if (addExtraShot) {
            component = new ExtraShotDecorator(component);
        }
        if (syrupFlavor != null && !syrupFlavor.isEmpty()) {
            component = new FlavorSyrupDecorator(component, syrupFlavor);
        }
        if (addWhippedCream) {
            component = new WhippedCreamDecorator(component);
        }
        if (addCaramel) {
            component = new CaramelDrizzleDecorator(component);
        }

        return new SingleOrder(component);
    }

    public ComboOrder createComboOrder(String comboName, double discount) {
        return new ComboOrder(comboName, discount);
    }

    public void printOrderSummary(OrderComponent order) {
        System.out.println("\n========== ORDER SUMMARY ==========");
        order.printOrder();
        System.out.println("-----------------------------------");
        System.out.println("Total Items: " + order.getItemCount());
        System.out.println("Total Cost: $" + String.format("%.2f", order.getTotalCost()));
        System.out.println("===================================\n");
    }

    public OrderComponent orderPopularCoffee(CoffeeType type, CoffeeOrderRequest request) {
        Coffee coffee = orderService.placeOrder(type, request);
        CoffeeComponent component = new BaseCoffeeComponent(coffee);

        // Add popular decorations based on coffee type
        switch (type) {
            case CAPPUCCINO:
                component = new WhippedCreamDecorator(component);
                component = new CaramelDrizzleDecorator(component);
                break;
            case LATTE:
                component = new FlavorSyrupDecorator(component, "Vanilla");
                break;
            case ESPRESSO:
                component = new ExtraShotDecorator(component);
                break;
        }

        return new SingleOrder(component);
    }
}
