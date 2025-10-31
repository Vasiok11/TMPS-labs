package lab2.domain;

import java.util.EnumMap;
import java.util.Map;

import lab2.factory.CoffeeFactory;
import lab2.models.Coffee;
import lab2.models.CoffeeType;

/**
 * Coordinates coffee orders by delegating instantiation to the registered factories.
 */
public class OrderService {
    private final Map<CoffeeType, CoffeeFactory> factories = new EnumMap<>(CoffeeType.class);

    public void registerFactory(CoffeeFactory factory) {
        factories.put(factory.supportedType(), factory);
    }

    public Coffee placeOrder(CoffeeType type, CoffeeOrderRequest request) {
        CoffeeFactory factory = factories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("No factory registered for type: " + type);
        }
        return factory.brewCoffee(request);
    }
}
