package com.epam.esm.service.validator;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Null;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ConstraintComposition(CompositionType.OR)
@Null
@ZeroConstraint
@ReportAsSingleViolation
@Target( { ElementType.METHOD, ElementType.FIELD } )
@Retention( RetentionPolicy.RUNTIME )
@Constraint(validatedBy = {FieldValidator.class})
public @interface FieldValidity {

    String message() default "Not valid";

    Class<?>[] groups() default { };

    Class< ? extends Payload>[] payload() default { };

    String leftLimit() default "1";

    String rightLimit() default "10";
}
