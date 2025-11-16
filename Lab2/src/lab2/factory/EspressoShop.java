package lab2.factory;

// Factory Method Pattern: Concrete Creator
public class EspressoShop extends CoffeeShop {
    
    @Override
    protected CoffeeFactory createFactory() {
        return new EspressoFactory();
    }
}
