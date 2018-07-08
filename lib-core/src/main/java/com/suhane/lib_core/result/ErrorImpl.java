package com.suhane.lib_core.result;

public class ErrorImpl implements Error{
    int errorCode;
    String errorMessage;

    public ErrorImpl(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
