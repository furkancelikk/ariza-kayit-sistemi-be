package com.furkancelik.arizakayitsistemi.annotation;
import com.furkancelik.arizakayitsistemi.validator.OneOfValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = { OneOfValidator.class})
public @interface OneOf {

    String message() default "En az birini seçiniz";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String[] values();
}
