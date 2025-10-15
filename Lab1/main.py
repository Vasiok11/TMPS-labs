from orders import CoffeeOrder
from pricing import PriceCalculator, extras_modifier, size_modifier
from service import CoffeeService


def main():
    base_prices = {"espresso": 2.5, "latte": 3.5, "cappuccino": 3.8}
    calculator = PriceCalculator(base_prices, [size_modifier, extras_modifier])

    stored_orders = []

    def save_to_memory(order, total):
        stored_orders.append((order, total))

    def notify_console(message):
        print(message)

    service = CoffeeService(calculator, save_to_memory, notify_console)

    beverage = input("Choose beverage (espresso/latte/cappuccino): ").strip().lower()
    size = input("Choose size (small/medium/large): ").strip().lower()
    extras = [extra.strip().lower() for extra in input("Extras (comma separated, blank for none): ").split(",") if extra.strip()]

    order = CoffeeOrder(beverage=beverage, size=size, extras=extras)
    total = service.process(order)
    print(f"Stored {len(stored_orders)} order(s). Last total: ${total}")


if __name__ == "__main__":
    main()
