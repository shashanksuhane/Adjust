package com.suhane.lib_cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.suhane.lib_core.event.Event;

import com.suhane.lib_cache.db.EventDbContract.PendingEventsEntry;
import com.suhane.lib_core.result.Result;
import com.suhane.lib_core.utils.EventUtils;

public class CacheImpl implements Cache{

    Context context;

    public CacheImpl(Context context) {
        this.context = context;
    }

    @Override
    public synchronized boolean save(Event event) {
        if (EventUtils.isValid(event).isSuccess() && context != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PendingEventsEntry.COLUMN_EVENT_TIME, event.getTimeInSec());

            if (!isExist(event)) {
                context.getContentResolver().insert(PendingEventsEntry.CONTENT_URI, contentValues);
            }
            return true;
        }
        return false;
    }

    @Override
    public synchronized Event getNextPendingEvent() {
        Event event = null;
        if (context == null) return event;

        Cursor cursor = context.getContentResolver().query(
                PendingEventsEntry.CONTENT_URI,
                new String[]{PendingEventsEntry.COLUMN_EVENT_TIME},
                null,
                null,
                PendingEventsEntry.COLUMN_EVENT_TIME);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                event = new Event(Long.parseLong(cursor.getString(0)));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return event;
    }

    @Override
    public synchronized boolean delete(Event event) {
        if (EventUtils.isValid(event).isSuccess() && context != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PendingEventsEntry.COLUMN_EVENT_TIME, event.getTimeInSec());

            context.getContentResolver().delete(PendingEventsEntry.CONTENT_URI,
                    PendingEventsEntry.COLUMN_EVENT_TIME + " = ?",
                    new String[]{String.valueOf(event.getTimeInSec())});
            return true;
        }
        return false;
    }

    private boolean isExist(Event event) {
        Cursor cursor = context.getContentResolver().query(
                PendingEventsEntry.CONTENT_URI,
                new String[]{PendingEventsEntry._ID},
                PendingEventsEntry.COLUMN_EVENT_TIME + " = ?",
                new String[]{String.valueOf(event.getTimeInSec())},
                null);
        return (cursor!=null && cursor.moveToFirst()==true);
    }
}
