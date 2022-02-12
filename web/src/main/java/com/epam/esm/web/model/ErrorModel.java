package com.epam.esm.web.model;

public class ErrorModel {

    private final int errorCode;
    private final String errorMessage;

    public ErrorModel(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
