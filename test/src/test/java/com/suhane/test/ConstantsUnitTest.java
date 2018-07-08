package com.suhane.test;

import com.suhane.lib_network.Constants;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

public class ConstantsUnitTest {
    @Test
    public void test_URL_valid() {
        assertNotNull(Constants.URL);
        assertFalse(Constants.URL.isEmpty());
    }

    @Test
    public void test_JSON_PARAMS_valid() {
        assertNotNull(Constants.HTTP_BODY_JSON_ID);
        assertFalse(Constants.HTTP_BODY_JSON_ID.isEmpty());

        assertNotNull(Constants.HTTP_BODY_JSON_SECONDS);
        assertFalse(Constants.HTTP_BODY_JSON_SECONDS.isEmpty());
    }
}
