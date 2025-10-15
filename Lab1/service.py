from orders import CoffeeOrder
from pricing import PriceCalculator


# Demonstrates DIP: depends on abstractions (callables) instead of concrete implementations.
class CoffeeService:
    def __init__(self, calculator, save_order, notify):
        self._calculator = calculator
        self._save_order = save_order
        self._notify = notify

    def process(self, order):
        total = self._calculator.calculate(order)
        self._save_order(order, total)
        self._notify(f"Order ready: {order.describe()} Total: ${total}")
        return total
