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
            String firstDateString = BeanUtils.getProperty(value, firstDateFieldName);
            String secondDateString = BeanUtils.getProperty(value, secondDateFieldName);

            if (firstDateString == null && secondDateString == null) {
                return true;
            } else if (firstDateString == null || secondDateString == null) {
                return false;
            }

            LocalDateTime firstDate = LocalDateTime.parse(firstDateString);
            LocalDateTime secondDate = LocalDateTime.parse(secondDateString);

            return firstDate.isEqual(secondDate) || firstDate.isBefore(secondDate);

        } catch (InvocationTargetException | IllegalAccessException |
                NoSuchMethodException e) {
            // ignore
        }
        return false;
    }
}
