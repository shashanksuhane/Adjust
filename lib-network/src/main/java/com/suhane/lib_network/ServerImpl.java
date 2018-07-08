package com.suhane.lib_network;


import com.suhane.lib_core.result.ErrorCodes;
import com.suhane.lib_core.result.ErrorMessages;
import com.suhane.lib_core.utils.EventUtils;
import com.suhane.lib_core.utils.TimeUtils;
import com.suhane.lib_core.event.Event;
import com.suhane.lib_core.result.ErrorImpl;
import com.suhane.lib_core.result.Result;
import com.suhane.lib_core.result.ResultImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServerImpl implements Server{

    @Override
    public Result post(final Event event) {

        Result result = EventUtils.isValid(event);
        if (!result.isSuccess()) return result;

        String url = Constants.URL;
        Map<String, String> header = new HashMap<>();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.HTTP_BODY_JSON_SECONDS, TimeUtils.getSecondInAMinute(event.getTimeInSec()));
        } catch (JSONException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setError(new ErrorImpl(ErrorCodes.JSON_CONVERSION_ERROR, ErrorMessages.JSON_CONVERSION_ERROR));
            return result;
        }

        String body = jsonObject.toString();
        String response = HTTPUtils.post(url, header, body);

        if (response != null) {
            result.setSuccess(true);
            result.setResponse(response);
        } else {
            result.setSuccess(false);
            result.setError(new ErrorImpl(ErrorCodes.HTTP_POST_FAILURE, ErrorMessages.HTTP_POST_FAILURE));
        }

        return result;
    }

}
