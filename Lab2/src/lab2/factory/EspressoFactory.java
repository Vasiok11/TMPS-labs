package lab2.factory;

import lab2.domain.CoffeeOrderRequest;
import lab2.models.Coffee;
import lab2.models.CoffeeRecipe;
import lab2.models.CoffeeType;

public class EspressoFactory extends RecipeBackedFactory {
    @Override
    public CoffeeType supportedType() {
        return CoffeeType.ESPRESSO;
    }

    @Override
    protected void applySpecialTouch(Coffee.Builder builder, CoffeeOrderRequest request, CoffeeRecipe recipe) {
        if (request.milkType().isEmpty()) {
            builder.milkType("None");
        }
    }
}
