package com.suhane.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.suhane.lib_core.result.Result;
import com.suhane.log_sdk.api.LogApi;
import com.suhane.log_sdk.api.LogApiImpl;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LogApiInstruTest {

    @Test
    public void test_init_result_fail() {
        LogApi api = new LogApiImpl(null);
        Result result = api.init();
        assertFalse(result.isSuccess());
    }

    @Test
    public void test_init_result_success() {
        LogApi api = new LogApiImpl(InstrumentationRegistry.getTargetContext());
        Result result = api.init();
        assertTrue(result.isSuccess());
    }

    @Test
    public void test_send_invalid_event() {
        LogApi api = new LogApiImpl(InstrumentationRegistry.getTargetContext());
        Result result = api.send(-1);
        assertFalse(result.isSuccess());
    }

    @Test
    public void test_send_duplicate_event() {
        LogApi api = new LogApiImpl(InstrumentationRegistry.getTargetContext());
        long currentTimeInSec = System.currentTimeMillis()/1000;
        Result result = api.send(currentTimeInSec);
        assertTrue(result.isSuccess());
        result = api.send(currentTimeInSec); //duplicate event
        assertFalse(result.isSuccess());
    }
}
