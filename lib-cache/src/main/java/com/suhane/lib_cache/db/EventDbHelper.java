package com.suhane.lib_cache.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.suhane.lib_cache.db.EventDbContract.PendingEventsEntry;

public class EventDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "events.db";

    public EventDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create pendingevent table
        final String SQL_CREATE_PENDING_EVENTS_TABLE = "CREATE TABLE " + PendingEventsEntry.TABLE_NAME + " (" +
                PendingEventsEntry._ID + " INTEGER PRIMARY KEY," +
                PendingEventsEntry.COLUMN_EVENT_TIME + " TEXT UNIQUE NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_PENDING_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
