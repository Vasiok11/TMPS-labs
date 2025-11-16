package lab2.factory;

// Factory Method Pattern: Concrete Creator
public class LatteShop extends CoffeeShop {
    
    @Override
    protected CoffeeFactory createFactory() {
        return new LatteFactory();
    }
}
