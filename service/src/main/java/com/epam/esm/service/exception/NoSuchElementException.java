package com.epam.esm.service.exception;

public class NoSuchElementException extends Exception {

    public NoSuchElementException() {
        super();
    }

    public NoSuchElementException(String message) {
        super(message);
    }

    public NoSuchElementException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchElementException(Throwable cause) {
        super(cause);
    }
}
