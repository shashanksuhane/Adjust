package com.suhane.lib_core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String getSecondInAMinute(long currentTimeInSec) {
        if (currentTimeInSec <= 0) return "00";

        SimpleDateFormat formatter = new SimpleDateFormat("ss");
        return formatter.format(new Date(currentTimeInSec*1000));
    }
}
