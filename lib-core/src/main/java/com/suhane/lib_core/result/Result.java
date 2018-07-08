package com.suhane.lib_core.result;

public interface Result {
    boolean isSuccess();
    String getResponse();
    Error getError();

    void setSuccess(boolean success);
    void setResponse(String response);
    void setError(Error error);
}
