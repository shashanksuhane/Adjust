package com.suhane.lib_cache.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class EventDbContract {
    public static final String CONTENT_AUTHORITY = "com.suhane.adjust";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PENDING_EVENTS = "pendingevents";

    public static final class PendingEventsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PENDING_EVENTS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PENDING_EVENTS;

        //Table Name
        public static final String TABLE_NAME = "pendingevents";
        //Column Names
        public static final String COLUMN_EVENT_TIME = "event_time";

        public static Uri buildPendingEventsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
