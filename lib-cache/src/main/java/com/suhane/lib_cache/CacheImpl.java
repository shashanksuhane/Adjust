package com.suhane.lib_cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.suhane.lib_cache.db.EventDbContract.PendingEventsEntry;
import com.suhane.lib_cache.prefs.LoggedEvents;
import com.suhane.lib_core.event.Event;
import com.suhane.lib_core.utils.EventUtils;
import com.suhane.lib_core.utils.TimeUtils;

import java.util.Set;

public class CacheImpl implements Cache{

    Context context;

    Set<String> loggedEvents;

    public CacheImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean init() {
        loggedEvents = new LoggedEvents(context).getLoggedEvents();
        return true;
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
                null,     // events are stored in the order they have occurred
                null,  // so selection criteria can be skipped
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

    @Override
    public boolean isDuplicate(Event event) {

        if (loggedEvents != null ) {
            String eventSeconds = TimeUtils.getSecondInAMinute(event.getTimeInSec());
            if (eventSeconds != null && !eventSeconds.isEmpty()) {
                if (!loggedEvents.isEmpty() && loggedEvents.contains(eventSeconds))
                    return true;
                else {
                    loggedEvents.add(eventSeconds);
                    new LoggedEvents(context).setLoggedEvents(loggedEvents);
                }
            }
        }
        return false;
    }

}
