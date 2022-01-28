package com.epam.esm.service.exception.impl;

import com.epam.esm.service.exception.ResourceCodeAccess;

public class ServiceException extends Exception implements ResourceCodeAccess {

    private int resourceCode;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
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
