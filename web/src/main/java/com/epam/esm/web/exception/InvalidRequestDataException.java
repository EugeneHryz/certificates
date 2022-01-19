package com.epam.esm.web.exception;

public class InvalidRequestDataException extends Exception {

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
}
