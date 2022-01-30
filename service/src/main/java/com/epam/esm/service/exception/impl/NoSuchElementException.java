package com.epam.esm.service.exception.impl;

import com.epam.esm.service.exception.ResourceCodeAccess;

public class NoSuchElementException extends Exception implements ResourceCodeAccess {

    private int resourceCode;

    public NoSuchElementException(int resourceCode) {
        super();
        this.resourceCode = resourceCode;
    }

    public NoSuchElementException(String message, int resourceCode) {
        super(message);
        this.resourceCode = resourceCode;
    }

    public NoSuchElementException(String message, Throwable cause, int resourceCode) {
        super(message, cause);
        this.resourceCode = resourceCode;
    }

    public NoSuchElementException(Throwable cause, int resourceCode) {
        super(cause);
        this.resourceCode = resourceCode;
    }

    @Override
    public int getResourceCode() {
        return resourceCode;
    }
}
