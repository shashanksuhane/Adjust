package com.suhane.lib_core.utils;

import com.suhane.lib_core.event.Event;
import com.suhane.lib_core.result.ErrorCodes;
import com.suhane.lib_core.result.ErrorImpl;
import com.suhane.lib_core.result.ErrorMessages;
import com.suhane.lib_core.result.Result;
import com.suhane.lib_core.result.ResultImpl;

public class EventUtils {
    public static Result isValid(Event event) {
        Result result = new ResultImpl();
        if (event == null || event.getTimeInSec() < 0) {
            result.setSuccess(false);
            result.setError(new ErrorImpl(ErrorCodes.INVALID_EVENT, ErrorMessages.INVALID_EVENT));
            return result;
        } else {
            result.setSuccess(true);
        }
        return result;
    }
}
