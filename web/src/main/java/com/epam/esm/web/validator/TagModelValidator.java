package com.epam.esm.web.validator;

import com.epam.esm.web.model.TagRequestModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class TagModelValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return TagRequestModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "name cannot be null");
        TagRequestModel tagModel = (TagRequestModel) target;

        String name = tagModel.getName();
        if (name.length() < 3 || name.length() > 100) {
            errors.rejectValue("name", "name must be between 3 and 100 characters long");
        }
    }
}
