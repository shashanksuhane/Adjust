package com.suhane.adjust;

import android.app.Application;

import com.suhane.log_sdk.api.LogApi;
import com.suhane.log_sdk.api.LogApiImpl;

public class AdjustApp extends Application {

    static private LogApi logApi;

    @Override
    public void onCreate() {
        super.onCreate();

        logApi = new LogApiImpl(getApplicationContext());
        logApi.init();
    }

    public static LogApi getLogAPI() {
        return logApi;
    }
}
