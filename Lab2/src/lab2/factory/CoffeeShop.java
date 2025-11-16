package lab2.factory;

import lab2.domain.CoffeeOrderRequest;
import lab2.models.Coffee;

// Factory Method Pattern: Abstract Creator
public abstract class CoffeeShop {
    
    protected abstract CoffeeFactory createFactory();
    
    public Coffee orderCoffee(CoffeeOrderRequest request) {
        CoffeeFactory factory = createFactory();
        return factory.brewCoffee(request);
    }
}
