package com.suhane.test;

import com.suhane.lib_core.utils.TimeUtils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

public class TimeUtilsUnitTest {
    @Test
    public void test_get_sec_invalid_time() {
        String sec = TimeUtils.getSecondInAMinute(-1);
        assertEquals("00", sec);
    }

    @Test
    public void test_get_sec_valid_time() {
        String sec = TimeUtils.getSecondInAMinute(System.currentTimeMillis()/1000);
        assertNotNull(sec);
        assertFalse(sec.isEmpty());
    }
}
