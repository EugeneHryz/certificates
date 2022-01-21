package com.epam.esm.web.exception.handler;

import com.epam.esm.service.exception.NoSuchElementException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.exception.Error;
import com.epam.esm.service.exception.InvalidRequestDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    private static final int CERTIFICATE_RESOURCE_CODE = 1;
    private static final int TAG_RESOURCE_CODE = 2;

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Error> resourceNotFound(NoSuchElementException e, HandlerMethod handlerMethod) {

        int resourceCode = CERTIFICATE_RESOURCE_CODE;
        if (handlerMethod.getBeanType().equals(TagController.class)) {
            resourceCode = TAG_RESOURCE_CODE;
        }
        return new ResponseEntity<>(new Error(
                HttpStatus.NOT_FOUND.value() * 100 + resourceCode, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Error> internalServerError(ServiceException e, HandlerMethod handlerMethod) {

        int resourceCode = CERTIFICATE_RESOURCE_CODE;
        if (handlerMethod.getBeanType().equals(TagController.class)) {
            resourceCode = TAG_RESOURCE_CODE;
        }
        return new ResponseEntity<>(new Error(
                HttpStatus.INTERNAL_SERVER_ERROR.value() * 100 + resourceCode, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRequestDataException.class)
    public ResponseEntity<Error> badRequestData(InvalidRequestDataException e, HandlerMethod handlerMethod) {

        List<String> defaultMessages = e.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        int resourceCode = CERTIFICATE_RESOURCE_CODE;
        if (handlerMethod.getBeanType().equals(TagController.class)) {
            resourceCode = TAG_RESOURCE_CODE;
        }
        return new ResponseEntity<>(new Error(
                HttpStatus.BAD_REQUEST.value() * 100 + resourceCode, defaultMessages.toString()), HttpStatus.BAD_REQUEST);
    }
}
