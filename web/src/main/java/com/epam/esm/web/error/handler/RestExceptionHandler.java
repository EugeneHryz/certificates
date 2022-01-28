package com.epam.esm.web.error.handler;

import com.epam.esm.service.exception.ResourceCodeAccess;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.web.error.Error;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({NoSuchElementException.class, ServiceException.class, InvalidRequestDataException.class})
    public ResponseEntity<Error> controllerException(Exception e) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof NoSuchElementException) {
            status = HttpStatus.NOT_FOUND;
        } else if (e instanceof InvalidRequestDataException) {
            status = HttpStatus.BAD_REQUEST;
        }

//        ResponseStatus statusAnnotation = AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class);
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//        if (statusAnnotation != null) {
//            status = statusAnnotation.value();
//        }

        return new ResponseEntity<>(new Error(
                status.value() * 100 + ((ResourceCodeAccess)e).getResourceCode(), e.getMessage()), status);
    }
}
