package lab2.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lab2.models.Size;

// Builder Pattern
public final class CoffeeOrderRequest {
    private final Size size;
    private final String milkType;
    private final List<String> addOns;
    private final boolean takeaway;

    private CoffeeOrderRequest(Builder builder) {
        this.size = builder.size;
        this.milkType = builder.milkType;
        this.addOns = List.copyOf(builder.addOns);
        this.takeaway = builder.takeaway;
    }

    public Optional<Size> size() {
        return Optional.ofNullable(size);
    }

    public Optional<String> milkType() {
        return Optional.ofNullable(milkType);
    }

    public List<String> addOns() {
        return Collections.unmodifiableList(addOns);
    }

    public boolean takeaway() {
        return takeaway;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Size size;
        private String milkType;
        private final List<String> addOns = new ArrayList<>();
        private boolean takeaway;

        private Builder() {
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

        public CoffeeOrderRequest build() {
            return new CoffeeOrderRequest(this);
        }
    }
}
