package com.epam.esm.service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DateValidator.class})
@Target({ElementType.TYPE})
public @interface DateValidity {

    String message() default "Invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String firstDate();
    String secondDate();
}
