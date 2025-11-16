package lab2.composite;

// Composite Pattern: Component Interface
public interface OrderComponent {
    String getDescription();
    double getTotalCost();
    void printOrder();
    int getItemCount();
}
