package lab2.factory;

import lab2.domain.CoffeeOrderRequest;
import lab2.models.Coffee;
import lab2.models.CoffeeRecipe;
import lab2.models.CoffeeType;
import lab2.models.Size;

public class CappuccinoFactory extends RecipeBackedFactory {
    @Override
    public CoffeeType supportedType() {
        return CoffeeType.CAPPUCCINO;
    }

    @Override
    protected void applySpecialTouch(Coffee.Builder builder, CoffeeOrderRequest request, CoffeeRecipe recipe) {
        if (request.takeaway() && request.size().isEmpty()) {
            builder.size(Size.LARGE);
        }
    }
}
