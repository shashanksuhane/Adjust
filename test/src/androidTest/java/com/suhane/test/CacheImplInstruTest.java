package com.suhane.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.suhane.lib_cache.Cache;
import com.suhane.lib_cache.CacheImpl;
import com.suhane.lib_core.event.Event;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CacheImplInstruTest {

    @Test
    public void test_save_invalid_event() {
        Cache cache = new CacheImpl(InstrumentationRegistry.getTargetContext());
        assertFalse(cache.save(null));
        assertFalse(cache.save(new Event(-1)));
    }

    @Test
    public void test_save_context_null() {
        Cache cache = new CacheImpl(null);
        assertFalse(cache.save(new Event(System.currentTimeMillis()/1000)));
    }

    @Test
    public void test_save_success() {
        Cache cache = new CacheImpl(InstrumentationRegistry.getTargetContext());
        assertTrue(cache.save(new Event(System.currentTimeMillis()/1000)));
    }

    @Test
    public void test_delete_invalid_event() {
        Cache cache = new CacheImpl(InstrumentationRegistry.getTargetContext());
        assertFalse(cache.delete(null));
        assertFalse(cache.delete(new Event(-1)));
    }

    @Test
    public void test_delete_context_null() {
        Cache cache = new CacheImpl(null);
        assertFalse(cache.delete(new Event(System.currentTimeMillis()/1000)));
    }

    @Test
    public void test_delete_success() {
        Cache cache = new CacheImpl(InstrumentationRegistry.getTargetContext());
        long currentTimeInSec = System.currentTimeMillis()/1000;
        Event event = new Event(currentTimeInSec);
        assertTrue(cache.save(event));
        assertTrue(cache.delete(event));
    }

    @Test
    public void test_getNextPendingEvent_context_null() {
        Cache cache = new CacheImpl(null);
        long currentTimeInSec = System.currentTimeMillis()/1000;
        Event event = new Event(currentTimeInSec);
        cache.save(event);
        assertNull(cache.getNextPendingEvent());
    }

    @Test
    public void test_getNextPendingEvent_event_exist() {
        Cache cache = new CacheImpl(InstrumentationRegistry.getTargetContext());
        long currentTimeInSec = System.currentTimeMillis()/1000;
        Event event = new Event(currentTimeInSec);
        cache.save(event);
        assertNotNull(cache.getNextPendingEvent());
    }

}
