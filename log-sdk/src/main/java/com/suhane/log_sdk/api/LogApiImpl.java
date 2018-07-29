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
import com.suhane.lib_network.Server;
import com.suhane.lib_network.ServerImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogApiImpl implements LogApi{

    private static final String TAG = "LogApi";

    Cache cache;
    Server server;
    ExecutorService pool;
    int lastRetry = 0;

    public LogApiImpl(Context context) {
        if (context == null) return;

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

        cache.init();

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
        final Event event = new Event(currentTimeInSec);
        Result result = EventUtils.isValid(event);
        if (!result.isSuccess()) return result;

        if (cache.isDuplicate(event)) {
            Log.w(TAG, TimeUtils.getSecondInAMinute(currentTimeInSec) + " " + ErrorMessages.DUPLICATE_REJECTED);
            result.setSuccess(false);
            result.setError(new ErrorImpl(ErrorCodes.DUPLICATE_REJECTED, ErrorMessages.DUPLICATE_REJECTED));
            return result;
        }

        pool.submit(new Runnable() {
            @Override
            public void run() {
                cache.save(event);
                sendPendingEvents();
            }
        });

        result.setSuccess(true);
        return result;
    }

    private synchronized void sendPendingEvents() {
        Event event = cache.getNextPendingEvent();

        while (event != null) {
            Result result = server.post(event);
            if (result.isSuccess()) {
                showResponse(result.getResponse());

                cache.delete(event);
                event = cache.getNextPendingEvent();
                lastRetry = 0;
            } else {
                scheduleForRetry(TimeUtils.getRetryTimeInSec(++lastRetry));
                event = null;
            }
        }
    }

    private void scheduleForRetry(final long retryTimeInSec) {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(retryTimeInSec * 1000);
                    sendPendingEvents();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

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
