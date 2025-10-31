package lab2.factory;

import lab2.domain.CoffeeOrderRequest;
import lab2.models.Coffee;
import lab2.models.CoffeeRecipe;
import lab2.models.CoffeeType;

public class LatteFactory extends RecipeBackedFactory {
    @Override
    public CoffeeType supportedType() {
        return CoffeeType.LATTE;
    }

    @Override
    protected void applySpecialTouch(Coffee.Builder builder, CoffeeOrderRequest request, CoffeeRecipe recipe) {
        // Lattes default to latte art sprinkle when enjoyed in house.
        if (!request.takeaway()) {
            builder.addOn("Latte Art");
        }
    }
}
