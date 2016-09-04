package net.java.javahub.backend;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@NotNull
@Constraint(validatedBy = NameValidator.class)
@Documented
public @interface ValidName {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
