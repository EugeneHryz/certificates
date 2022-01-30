package com.epam.esm.service.exception.impl;

import com.epam.esm.service.exception.ResourceCodeAccess;

public class ServiceException extends Exception implements ResourceCodeAccess {

    private int resourceCode;

    public ServiceException(int resourceCode) {
        super();
        this.resourceCode = resourceCode;
    }

    public ServiceException(String message, int resourceCode) {
        super(message);
        this.resourceCode = resourceCode;
    }

    public ServiceException(String message, Throwable cause, int resourceCode) {
        super(message, cause);
        this.resourceCode = resourceCode;
    }

    public ServiceException(Throwable cause, int resourceCode) {
        super(cause);
        this.resourceCode = resourceCode;
    }

    @Override
    public int getResourceCode() {
        return resourceCode;
    }
}
