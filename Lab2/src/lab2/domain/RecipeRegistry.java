package lab2.domain;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import lab2.models.CoffeeRecipe;
import lab2.models.CoffeeType;
import lab2.models.Size;

// Singleton Pattern
public final class RecipeRegistry {
    private static final RecipeRegistry INSTANCE = new RecipeRegistry();

    private final Map<CoffeeType, CoffeeRecipe> recipes = new EnumMap<>(CoffeeType.class);

    private RecipeRegistry() {
        loadDefaultRecipes();
    }

    public static RecipeRegistry getInstance() {
        return INSTANCE;
    }

    public CoffeeRecipe lookup(CoffeeType type) {
        CoffeeRecipe recipe = recipes.get(type);
        if (recipe == null) {
            throw new IllegalArgumentException("No recipe registered for type: " + type);
        }
        return recipe.clone();
    }

    private void loadDefaultRecipes() {
        recipes.put(
            CoffeeType.ESPRESSO,
            new CoffeeRecipe(
                CoffeeType.ESPRESSO.getDisplayName(),
                Size.SMALL,
                "None",
                List.of()
            )
        );
        recipes.put(
            CoffeeType.LATTE,
            new CoffeeRecipe(
                CoffeeType.LATTE.getDisplayName(),
                Size.MEDIUM,
                "Oat",
                List.of("Vanilla Syrup")
            )
        );
        recipes.put(
            CoffeeType.CAPPUCCINO,
            new CoffeeRecipe(
                CoffeeType.CAPPUCCINO.getDisplayName(),
                Size.MEDIUM,
                "Whole",
                List.of("Cocoa Powder")
            )
        );
    }
}
