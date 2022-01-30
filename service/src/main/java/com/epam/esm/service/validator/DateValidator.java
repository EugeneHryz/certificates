package com.epam.esm.service.validator;

import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

public class DateValidator implements ConstraintValidator<DateValidity, Object> {

    private String firstDateFieldName;
    private String secondDateFieldName;

    @Override
    public void initialize(DateValidity constraintAnnotation) {
        firstDateFieldName = constraintAnnotation.firstDate();
        secondDateFieldName = constraintAnnotation.secondDate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            String firstDateField = BeanUtils.getProperty(value, firstDateFieldName);
            String secondDateField = BeanUtils.getProperty(value, secondDateFieldName);

            if (firstDateField == null || secondDateField == null) {
                return true;
            }
            LocalDateTime firstDate = LocalDateTime.parse(firstDateField);
            LocalDateTime secondDate = LocalDateTime.parse(secondDateField);

            return firstDate.isEqual(secondDate) || firstDate.isBefore(secondDate);

        } catch (InvocationTargetException | IllegalAccessException |
                NoSuchMethodException e) {
            // ignore
        }
        return false;
    }
}
