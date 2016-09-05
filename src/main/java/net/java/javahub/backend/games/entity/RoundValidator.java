package net.java.javahub.backend.games.entity;

import javax.json.JsonObject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoundValidator implements ConstraintValidator<ValidRound, JsonObject> {

    public void initialize(ValidRound constraint) {
    }

    public boolean isValid(JsonObject object, ConstraintValidatorContext context) {
        final String deviceId = object.getString("deviceId", null);
        return deviceId != null && !deviceId.isEmpty();
    }

}
