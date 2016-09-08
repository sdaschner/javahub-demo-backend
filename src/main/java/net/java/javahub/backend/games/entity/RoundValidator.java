package net.java.javahub.backend.games.entity;

import javax.json.JsonObject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoundValidator implements ConstraintValidator<ValidRound, JsonObject> {

    public void initialize(ValidRound constraint) {
    }

    public boolean isValid(JsonObject object, ConstraintValidatorContext context) {
        final String device = object.getString("device", null);
        return device != null && !device.isEmpty();
    }

}
