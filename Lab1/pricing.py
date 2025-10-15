from orders import CoffeeOrder


# Demonstrates OCP: new pricing modifiers can be registered without changing this class.
class PriceCalculator:
    def __init__(self, base_prices, modifiers=None):
        self._base_prices = base_prices
        self._modifiers = list(modifiers or [])

    def add_modifier(self, modifier):
        self._modifiers.append(modifier)

    def calculate(self, order):
        if order.beverage not in self._base_prices:
            raise ValueError(f"Unknown beverage: {order.beverage}")
        total = self._base_prices[order.beverage]
        for modifier in self._modifiers:
            total = modifier(order, total)
        return round(total, 2)


def size_modifier(order, running_total):
    size_adjustments = {"small": 0.0, "medium": 0.5, "large": 1.0}
    adjustment = size_adjustments.get(order.size.lower())
    if adjustment is None:
        raise ValueError(f"Unknown size: {order.size}")
    return running_total + adjustment


def extras_modifier(order, running_total):
    extras_prices = {"soy": 0.4, "oat": 0.5, "vanilla": 0.6, "caramel": 0.7}
    extras_total = sum(extras_prices.get(extra.lower(), 0.0) for extra in order.extras)
    return running_total + extras_total
