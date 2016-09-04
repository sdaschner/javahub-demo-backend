package net.java.javahub.backend.coffee.entity;

import net.java.javahub.backend.coffee.boundary.CoffeeShop;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrderValidator implements ConstraintValidator<ValidOrder, JsonObject> {

    @Inject
    CoffeeShop coffeeShop;

    public void initialize(ValidOrder constraint) {
    }

    public boolean isValid(JsonObject object, ConstraintValidatorContext context) {
        final String type = object.getString("type", null);
        if (type == null || coffeeShop.getType(type) == null)
            return false;

        final int strength = object.getInt("strength", 0);
        if (strength < 1 || strength > 10)
            return false;

        return true;
    }

}
