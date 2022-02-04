package com.epam.esm.web.validator;

import com.epam.esm.web.model.GiftCertificateRequestModel;
import com.epam.esm.web.model.TagRequestModel;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CertificateModelValidator implements Validator {

    private TagModelValidator tagValidator;

    public CertificateModelValidator(TagModelValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return GiftCertificateRequestModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GiftCertificateRequestModel certificateModel =
                (GiftCertificateRequestModel) target;

        String name = certificateModel.getName();
        if (name != null && (name.length() < 5 || name.length() > 200)) {
            errors.rejectValue("name", "name must be between 5 and 200 characters in length. " +
                    "(null if need to be unchanged)");
        }
        String description = certificateModel.getDescription();
        if (description != null && (description.length() < 5 || description.length() > 200)) {
            errors.rejectValue("description", "description must be between 5 and 200 characters in length. " +
                    "(null if need to be unchanged)");
        }

        double price = certificateModel.getPrice();
        if (price != 0.0 && (Double.compare(price, 0.5) < 0 || Double.compare(price, 10000.0) > 0)) {
            errors.rejectValue("price", "price must be between 0.5 and 10000.0. " +
                    "(value 0.0 if need to be unchanged)");
        }
        int duration = certificateModel.getDuration();
        if (duration != 0 && (duration < 3 || duration > 365)) {
            errors.rejectValue("duration", "duration must be between 3 and 365. " +
                    "(value 0 if need to be unchanged)");
        }

        LocalDateTime created = certificateModel.getCreated();
        LocalDateTime lastUpdated = certificateModel.getLastUpdated();
        if (created != null && lastUpdated != null) {
            if (lastUpdated.isBefore(created)) {
                errors.rejectValue("created", "'created' date cannot be after 'lastUpdated' date");
            }
        }

        List<TagRequestModel> tags = certificateModel.getTags();
        if (tags != null && !tags.isEmpty()) {
            int index = 0;
            for (TagRequestModel tagModel : tags) {
                errors.pushNestedPath("tags[" + index + "]");
                ValidationUtils.invokeValidator(this.tagValidator, tagModel, errors);
                errors.popNestedPath();
                index++;
            }
        }
    }
}
