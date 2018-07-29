package com.suhane.lib_core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String getSecondInAMinute(long currentTimeInSec) {
        if (currentTimeInSec <= 0) return "00";

        SimpleDateFormat formatter = new SimpleDateFormat("ss");
        return formatter.format(new Date(currentTimeInSec*1000));
    }

    public static long getRetryTimeInSec(int numRetries) {
        long retryInSec = 0;
        for (int i=1; i<=numRetries; i++) {
            retryInSec = retryInSec + getFibValue(i);
            if (retryInSec >= 10*60) {
                return 10*60;
            }
        }
        return retryInSec;
    }

    private static long getFibValue(int numRetries) {
        if (numRetries == 1 || numRetries ==2) return 1;
        else return getFibValue(numRetries-1) + getFibValue(numRetries -2);
    }
}
