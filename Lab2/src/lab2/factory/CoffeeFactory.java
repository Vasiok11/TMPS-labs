package lab2.factory;

import lab2.domain.CoffeeOrderRequest;
import lab2.models.Coffee;
import lab2.models.CoffeeType;

// Factory Method Pattern: Product Interface
public interface CoffeeFactory {
    Coffee brewCoffee(CoffeeOrderRequest request);

    CoffeeType supportedType();
}
