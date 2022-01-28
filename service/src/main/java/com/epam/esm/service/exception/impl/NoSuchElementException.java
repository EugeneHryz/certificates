package com.epam.esm.service.exception.impl;

import com.epam.esm.service.exception.ResourceCodeAccess;

public class NoSuchElementException extends Exception implements ResourceCodeAccess {

    private int resourceCode;

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

    @Override
    public int getResourceCode() {
        return resourceCode;
    }

    @Override
    public void setResourceCode(int code) {
        resourceCode = code;
    }
}
