package lab2.models;

import java.util.List;

// Prototype Pattern
public class CoffeeRecipe implements Cloneable {
    private final String name;
    private final Size defaultSize;
    private final String defaultMilk;
    private final List<String> defaultAddOns;

    public CoffeeRecipe(String name, Size defaultSize, String defaultMilk, List<String> defaultAddOns) {
        this.name = name;
        this.defaultSize = defaultSize;
        this.defaultMilk = defaultMilk;
        this.defaultAddOns = List.copyOf(defaultAddOns);
    }

    public String getName() {
        return name;
    }

    public Size getDefaultSize() {
        return defaultSize;
    }

    public String getDefaultMilk() {
        return defaultMilk;
    }

    public List<String> getDefaultAddOns() {
        return List.copyOf(defaultAddOns);
    }

    @Override
    public CoffeeRecipe clone() {
        return new CoffeeRecipe(name, defaultSize, defaultMilk, defaultAddOns);
    }
}
