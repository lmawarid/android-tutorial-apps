package com.example.luthfi.notepad.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Luthfi on 9/7/2016.
 */

public class NotepadProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static final int NOTE = 100;
    private static final int NOTE_ID = 101;

    private NotepadDbHelper openHelper;

    private static final SQLiteQueryBuilder queryBuilder;

    static {
        queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(NotepadContract.NoteEntry.TABLE_NAME);
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NotepadContract.AUTHORITY;

        matcher.addURI(authority, NotepadContract.PATH_NOTE, NOTE);
        matcher.addURI(authority, NotepadContract.PATH_NOTE + "/#", NOTE_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        openHelper = new NotepadDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        SQLiteDatabase db = openHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case NOTE:
                returnCursor = db.query(
                        NotepadContract.NoteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case NOTE_ID:
                returnCursor = db.query(
                        NotepadContract.NoteEntry.TABLE_NAME,
                        projection,
                        NotepadContract.NoteEntry._ID + "=" + ContentUris.parseId(uri),
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case NOTE:
                return NotepadContract.NoteEntry.CONTENT_TYPE;
            case NOTE_ID:
                return NotepadContract.NoteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();

        Uri returnUri;
        switch (uriMatcher.match(uri)) {
            case NOTE:
                long _id = db.insert(NotepadContract.NoteEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) returnUri = ContentUris.withAppendedId(NotepadContract.NoteEntry.CONTENT_URI, _id);
                else throw new SQLiteException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        int rowsDeleted;

        switch (uriMatcher.match(uri)) {
            case NOTE:
                rowsDeleted = db.delete(NotepadContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTE_ID:
                rowsDeleted = db.delete(NotepadContract.NoteEntry.TABLE_NAME,
                        NotepadContract.NoteEntry._ID + "='" + ContentUris.parseId(uri) + "'",
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        int rowsUpdated;

        switch (uriMatcher.match(uri)) {
            case NOTE:
                rowsUpdated = db.update(NotepadContract.NoteEntry.TABLE_NAME,
                        contentValues, selection, selectionArgs);
                break;
            case NOTE_ID:
                rowsUpdated = db.update(NotepadContract.NoteEntry.TABLE_NAME,
                        contentValues,
                        NotepadContract.NoteEntry._ID + "='" + ContentUris.parseId(uri) + "'",
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (selection == null || rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
