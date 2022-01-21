package com.epam.esm.service.exception;

import org.springframework.validation.BindingResult;

public class InvalidRequestDataException extends Exception {

    private BindingResult bindingResult;

    public InvalidRequestDataException() {
        super();
    }

    public InvalidRequestDataException(String message) {
        super(message);
    }

    public InvalidRequestDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestDataException(Throwable cause) {
        super(cause);
    }

    public InvalidRequestDataException(BindingResult result) {
        bindingResult = result;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
