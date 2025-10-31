package lab2.factory;

import lab2.domain.CoffeeOrderRequest;
import lab2.models.Coffee;
import lab2.models.CoffeeType;

/**
 * Abstract Factory for brewing specific coffee types.
 */
public interface CoffeeFactory {
    Coffee brewCoffee(CoffeeOrderRequest request);

    CoffeeType supportedType();
}
