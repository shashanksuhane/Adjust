package com.suhane.lib_cache.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.suhane.lib_cache.db.EventDbContract.PendingEventsEntry;

public class EventDbContentProvider extends ContentProvider{

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private EventDbHelper mDbHelper;

    static final int PENDING_EVENTS = 100;

    private static final SQLiteQueryBuilder sqliteQueryBuilder;

    static {
        sqliteQueryBuilder = new SQLiteQueryBuilder();
        sqliteQueryBuilder.setTables(
                PendingEventsEntry.TABLE_NAME);
        sqliteQueryBuilder.setTables(
                PendingEventsEntry.TABLE_NAME);
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = EventDbContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, EventDbContract.PATH_PENDING_EVENTS, PENDING_EVENTS);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new EventDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch ((sUriMatcher.match(uri))){
            case PENDING_EVENTS: {
                cursor = mDbHelper.getReadableDatabase().query(
                        PendingEventsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch(match) {
            case PENDING_EVENTS: {
                return PendingEventsEntry.CONTENT_TYPE;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri insertUri;

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PENDING_EVENTS: {
                long id = db.insert(PendingEventsEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    insertUri = PendingEventsEntry.buildPendingEventsUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return insertUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted;

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PENDING_EVENTS: {
                rowsDeleted = db.delete(PendingEventsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
