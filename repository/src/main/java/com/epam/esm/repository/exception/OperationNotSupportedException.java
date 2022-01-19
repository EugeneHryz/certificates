package com.epam.esm.repository.exception;

public class OperationNotSupportedException extends RuntimeException {

    public OperationNotSupportedException() {
        super();
    }

    public OperationNotSupportedException(String message) {
        super(message);
    }

    public OperationNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperationNotSupportedException(Throwable cause) {
        super(cause);
    }
}
