from dataclasses import dataclass


# Demonstrates SRP: this class is only responsible for representing a coffee order.
@dataclass
class CoffeeOrder:
    beverage: str
    size: str
    extras: list

    def describe(self) -> str:
        extras_part = ", ".join(self.extras) if self.extras else "no extras"
        return f"{self.size.title()} {self.beverage.title()} with {extras_part}."
