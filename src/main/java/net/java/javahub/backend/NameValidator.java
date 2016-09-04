package net.java.javahub.backend;

import javax.json.JsonObject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidName, JsonObject> {

    public void initialize(ValidName constraint) {
    }

    public boolean isValid(JsonObject object, ConstraintValidatorContext context) {
        final String name = object.getString("name", null);
        return name != null && !name.isEmpty();
    }

}
