package com.suhane.test;

import android.support.test.runner.AndroidJUnit4;

import com.suhane.lib_core.utils.TimeUtils;
import com.suhane.lib_network.Constants;
import com.suhane.lib_network.HTTPUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class HTTPUtilsInstruTest {
    @Test
    public void test_post_url_null() {
        String response = HTTPUtils.post(null, null, "body");
        assertNull(response);
    }

    @Test
    public void test_post_url_empty() {
        String response = HTTPUtils.post("", null, "body");
        assertNull(response);
    }

    @Test
    public void test_post_body_null() {
        String response = HTTPUtils.post(Constants.URL, null, null);
        assertNull(response);
    }

    @Test
    public void test_post_body_empty() {
        String response = HTTPUtils.post(Constants.URL, null, "");
        assertNull(response);
    }

    @Test
    public void test_post_url_invalid() {

        String body = getBody(System.currentTimeMillis()/1000);
        String response = HTTPUtils.post("https://my.url", null, body);

        assertNull(response);
    }

    @Test
    public void test_post_url_valid() {

        long currentTimeInSec = System.currentTimeMillis()/1000;
        String body = getBody(currentTimeInSec);
        String response = HTTPUtils.post(Constants.URL, null, body);

        assertNotNull(response);
        assertFalse(response.isEmpty());

        try {
            JSONObject jsonObject = new JSONObject(response);
            String actualSec = jsonObject.getString(Constants.HTTP_BODY_JSON_SECONDS);
            String expectedSec = TimeUtils.getSecondInAMinute(currentTimeInSec);
            assertEquals(expectedSec, actualSec);
        } catch (JSONException e) {
            assertTrue(false);
        }


    }

    private String getBody(long currentTimeInSec) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.HTTP_BODY_JSON_SECONDS, TimeUtils.getSecondInAMinute(currentTimeInSec));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
