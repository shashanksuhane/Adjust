package com.suhane.lib_cache.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class LoggedEvents {

    private static final String PREFS_NAME = "PREFS_EVENTS_LOGGED";
    private static final String STRING_SET = "SET_EVENTS_LOGGED";

    private Context context;

    public LoggedEvents(Context context) {
        this.context = context;
    }

    public boolean setLoggedEvents(Set<String> loggedEventSet) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putStringSet(STRING_SET, loggedEventSet);
        return edit.commit();
    }

    public Set<String> getLoggedEvents() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> stringSet = sharedPreferences.getStringSet(STRING_SET, new HashSet<String>());
        return stringSet;
    }
}
