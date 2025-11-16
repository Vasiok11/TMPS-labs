package lab2.domain;

import java.util.EnumMap;
import java.util.Map;

import lab2.factory.CappuccinoShop;
import lab2.factory.CoffeeShop;
import lab2.factory.EspressoShop;
import lab2.factory.LatteShop;
import lab2.models.Coffee;
import lab2.models.CoffeeType;

public class OrderService {
    private final Map<CoffeeType, CoffeeShop> shops = new EnumMap<>(CoffeeType.class);

    public OrderService() {
        shops.put(CoffeeType.ESPRESSO, new EspressoShop());
        shops.put(CoffeeType.LATTE, new LatteShop());
        shops.put(CoffeeType.CAPPUCCINO, new CappuccinoShop());
    }

    public Coffee placeOrder(CoffeeType type, CoffeeOrderRequest request) {
        CoffeeShop shop = shops.get(type);
        if (shop == null) {
            throw new IllegalArgumentException("No shop available for type: " + type);
        }
        return shop.orderCoffee(request);
    }
}
