package com.suhane.test;

import android.support.test.runner.AndroidJUnit4;

import com.suhane.lib_core.event.Event;
import com.suhane.lib_core.result.Result;
import com.suhane.lib_network.Server;
import com.suhane.lib_network.ServerImpl;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ServerImplInstruTest {
    @Test
    public void test_post_invalid_event() {
        Server server = new ServerImpl();
        Event event = new Event(-1);
        Result result = server.post(event);
        assertFalse(result.isSuccess());
    }

    @Test
    public void test_post_valid_event() {
        Server server = new ServerImpl();
        Event event = new Event(System.currentTimeMillis()/1000);
        Result result = server.post(event);
        assertTrue(result.isSuccess());
    }
}
