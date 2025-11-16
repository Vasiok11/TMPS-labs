package lab2.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// Builder Pattern
public final class Coffee {
    private final String name;
    private final Size size;
    private final String milkType;
    private final List<String> addOns;
    private final boolean takeaway;

    private Coffee(Builder builder) {
        this.name = builder.name;
        this.size = builder.size;
        this.milkType = builder.milkType;
        this.addOns = List.copyOf(builder.addOns);
        this.takeaway = builder.takeaway;
    }

    public String getName() {
        return name;
    }

    public Size getSize() {
        return size;
    }

    public String getMilkType() {
        return milkType;
    }

    public List<String> getAddOns() {
        return Collections.unmodifiableList(addOns);
    }

    public boolean isTakeaway() {
        return takeaway;
    }

    @Override
    public String toString() {
        return "Coffee{" +
            "name='" + name + '\'' +
            ", size=" + size +
            ", milkType='" + milkType + '\'' +
            ", addOns=" + addOns +
            ", takeaway=" + takeaway +
            '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private Size size = Size.MEDIUM;
        private String milkType = "Whole";
        private final List<String> addOns = new ArrayList<>();
        private boolean takeaway;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = Objects.requireNonNull(name, "name");
            return this;
        }

        public Builder size(Size size) {
            this.size = Objects.requireNonNull(size, "size");
            return this;
        }

        public Builder milkType(String milkType) {
            this.milkType = Objects.requireNonNull(milkType, "milkType");
            return this;
        }

        public Builder addOn(String addOn) {
            this.addOns.add(Objects.requireNonNull(addOn, "addOn"));
            return this;
        }

        public Builder addOns(List<String> addOns) {
            this.addOns.addAll(Objects.requireNonNull(addOns, "addOns"));
            return this;
        }

        public Builder takeaway(boolean takeaway) {
            this.takeaway = takeaway;
            return this;
        }

        public Coffee build() {
            if (name == null || name.isBlank()) {
                throw new IllegalStateException("Coffee name must be provided");
            }
            return new Coffee(this);
        }
    }
}
