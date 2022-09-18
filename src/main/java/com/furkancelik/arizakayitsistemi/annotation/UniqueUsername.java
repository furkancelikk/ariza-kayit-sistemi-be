package com.furkancelik.arizakayitsistemi.annotation;

import com.furkancelik.arizakayitsistemi.validator.UniqueUsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = { UniqueUsernameValidator.class})
public @interface UniqueUsername {

    String message() default "{custom.constraints.username.Unique.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
