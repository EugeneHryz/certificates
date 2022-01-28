package com.epam.esm.service.exception.impl;

import com.epam.esm.service.exception.ResourceCodeAccess;

public class InvalidRequestDataException extends Exception implements ResourceCodeAccess {

    private int resourceCode;

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

    @Override
    public int getResourceCode() {
        return resourceCode;
    }

    @Override
    public void setResourceCode(int code) {
        resourceCode = code;
    }
}
