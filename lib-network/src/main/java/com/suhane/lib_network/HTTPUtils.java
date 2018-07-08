package com.suhane.lib_network;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class HTTPUtils {
    public static String post(String sUrl, Map<String, String> headers, String body) {
        String response = null;

        if (sUrl == null || sUrl.isEmpty() || body == null || body.isEmpty())
            return response;

        ByteArrayOutputStream output = null;
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(sUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            addAdditionalHeaders(connection, headers);

            connection.setRequestMethod("POST");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();

            if (isResponseSuccess(connection.getResponseCode())) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                response = sb.toString();
            }

         } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return response;
   }

    private static boolean isResponseSuccess(int responseCode) {
        if (responseCode >= 200 && responseCode < 300)
            return true;
        return false;
    }

    private static void addAdditionalHeaders(HttpsURLConnection connection, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                connection.setRequestProperty(key, headers.get(key));
            }
        }
    }
}
