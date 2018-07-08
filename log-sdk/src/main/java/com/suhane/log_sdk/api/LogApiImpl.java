package com.suhane.log_sdk.api;

import android.content.Context;
import android.util.Log;

import com.suhane.lib_cache.Cache;
import com.suhane.lib_cache.CacheImpl;
import com.suhane.lib_core.event.Event;
import com.suhane.lib_core.result.ErrorCodes;
import com.suhane.lib_core.result.ErrorImpl;
import com.suhane.lib_core.result.ErrorMessages;
import com.suhane.lib_core.result.Result;
import com.suhane.lib_core.result.ResultImpl;
import com.suhane.lib_core.utils.EventUtils;
import com.suhane.lib_core.utils.TimeUtils;
import com.suhane.lib_network.Constants;
import com.suhane.lib_network.HTTPUtils;
import com.suhane.lib_network.Server;
import com.suhane.lib_network.ServerImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogApiImpl implements LogApi{

    private static final String TAG = "LogApi";

    long lastCurrentTimeInSec;
    Cache cache;
    Server server;
    ExecutorService pool;

    public LogApiImpl(Context context) {
        if (context == null) return;

        lastCurrentTimeInSec = 0;
        cache = new CacheImpl(context);
        server = new ServerImpl();
        pool = Executors.newSingleThreadExecutor();
    }

    @Override
    public Result init() {

        Result result = new ResultImpl();

        if (pool == null || cache == null || server == null) {
            result.setSuccess(false);
            return result;
        }

        pool.submit(new Runnable() {
            @Override
            public void run() {
                sendPendingEvents();
            }
        });

        result.setSuccess(true);
        return result;
    }

    @Override
    public Result send(final long currentTimeInSec) {
        Event event = new Event(currentTimeInSec);
        Result result = EventUtils.isValid(event);
        if (!result.isSuccess()) return result;

        if (isDuplicate(currentTimeInSec)) {
            Log.w(TAG, TimeUtils.getSecondInAMinute(currentTimeInSec) + " " + ErrorMessages.DUPLICATE_REJECTED);
            result.setSuccess(false);
            result.setError(new ErrorImpl(ErrorCodes.DUPLICATE_REJECTED, ErrorMessages.DUPLICATE_REJECTED));
            return result;
        }

        lastCurrentTimeInSec = currentTimeInSec;
        cache.save(event);

        pool.submit(new Runnable() {
            @Override
            public void run() {
                sendPendingEvents();
            }
        });

        result.setSuccess(true);
        return result;
    }

    private boolean isDuplicate(long currentTimeInSec) {
        // there can be other better ways of identifying the duplicate request
        // like querying from db etc, but for this particular requirement where
        // to identify duplicate based on instant time in seconds,
        // below implementation should be good enough

        if (lastCurrentTimeInSec == currentTimeInSec)
            return true;

        return false;
    }

    private synchronized void sendPendingEvents() {
        Event event = cache.getNextPendingEvent();

        while (event != null) {
            Result result = server.post(event);
            if (result.isSuccess()) {
                showResponse(result.getResponse());

                cache.delete(event);
                event = cache.getNextPendingEvent();
            } else {
                event = null;
            }
        }
    }

    private void showResponse(String jsonResponse) {
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                String seconds = jsonObject.getString(Constants.HTTP_BODY_JSON_SECONDS);
                String id = jsonObject.getString(Constants.HTTP_BODY_JSON_ID);
                Log.i(TAG, "Response:id="+id+",seconds="+seconds);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
