package com.epam.esm.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldValidator implements ConstraintValidator<FieldValidity, Object> {

    private String leftLimit;
    private String rightLimit;

    @Override
    public void initialize(FieldValidity constraintAnnotation) {
        leftLimit = constraintAnnotation.leftLimit();
        rightLimit = constraintAnnotation.rightLimit();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean result = false;

        if (value instanceof String) {
            int minSize = Integer.parseInt(leftLimit);
            int maxSize = Integer.parseInt(rightLimit);

            int actualLength = ((String)value).length();
            result = actualLength >= minSize && actualLength <= maxSize;

        } else if (value instanceof Integer) {
            int min = Integer.parseInt(leftLimit);
            int max = Integer.parseInt(rightLimit);

            int actualValue = (Integer)value;
            result = actualValue >= min && actualValue <= max;

        } else if (value instanceof Double) {
            double min = Double.parseDouble(leftLimit);
            double max = Double.parseDouble(rightLimit);

            double actualValue = (Double)value;
            result = Double.compare(actualValue, min) >= 0 && Double.compare(actualValue, max) <= 0;
        }
        return result;
    }
}
