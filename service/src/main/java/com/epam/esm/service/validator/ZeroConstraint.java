package com.epam.esm.service.validator;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ConstraintComposition(CompositionType.AND)
@Min(value = 0)
@Max(value = 0)
@Target( { ElementType.ANNOTATION_TYPE } )
@Retention( RetentionPolicy.RUNTIME )
@Constraint(validatedBy = { })
@interface ZeroConstraint {

    String message() default "Not valid";

    Class<?>[] groups() default { };

    Class< ? extends Payload>[] payload() default { };
}
