package lab2.factory;

import lab2.domain.CoffeeOrderRequest;
import lab2.domain.RecipeRegistry;
import lab2.models.Coffee;
import lab2.models.CoffeeRecipe;
import lab2.models.CoffeeType;

/**
 * Reusable template for factories that rely on recipes from the registry.
 */
abstract class RecipeBackedFactory implements CoffeeFactory {
    private final RecipeRegistry registry = RecipeRegistry.getInstance();

    @Override
    public Coffee brewCoffee(CoffeeOrderRequest request) {
        CoffeeRecipe recipe = registry.lookup(supportedType());

        Coffee.Builder builder = Coffee.builder()
            .name(recipe.getName())
            .size(request.size().orElse(recipe.getDefaultSize()))
            .milkType(request.milkType().orElse(recipe.getDefaultMilk()))
            .takeaway(request.takeaway());

        if (!recipe.getDefaultAddOns().isEmpty()) {
            builder.addOns(recipe.getDefaultAddOns());
        }
        if (!request.addOns().isEmpty()) {
            builder.addOns(request.addOns());
        }

        applySpecialTouch(builder, request, recipe);
        return builder.build();
    }

    /**
     * Hook for custom adjustments specific to a coffee type.
     */
    protected void applySpecialTouch(Coffee.Builder builder, CoffeeOrderRequest request, CoffeeRecipe recipe) {
        // default does nothing
    }

    @Override
    public abstract CoffeeType supportedType();
}
