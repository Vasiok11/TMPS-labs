package lab2.factory;

// Factory Method Pattern: Concrete Creator
public class CappuccinoShop extends CoffeeShop {
    
    @Override
    protected CoffeeFactory createFactory() {
        return new CappuccinoFactory();
    }
}
