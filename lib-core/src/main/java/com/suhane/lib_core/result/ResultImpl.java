package com.suhane.lib_core.result;

public class ResultImpl implements Result{
    private boolean isSuccess;
    private String response;
    private Error error;

    public ResultImpl(boolean success, String response, Error error) {
        this.isSuccess = success;
        this.response = response;
        this.error = error;
    }

    public ResultImpl(boolean success, String response) {
        this(success, response, null);
    }

    public ResultImpl(boolean success) {
        this(success, "", null);
    }

    public ResultImpl(boolean success, Error error) {
        this(success, "", error);
    }

    public ResultImpl() {
        this(false, "", null);
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public String getResponse() {
        return response;
    }

    @Override
    public Error getError() {
        return error;
    }

    @Override
    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @Override
    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public void setError(Error error) {
        this.error = error;
    }
}
