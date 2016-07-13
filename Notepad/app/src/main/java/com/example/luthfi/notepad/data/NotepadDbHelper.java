package com.example.luthfi.notepad.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Luthfi on 9/7/2016.
 */
public class NotepadDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notepad.db";

    public NotepadDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Make sure to create a unique ID for the table.
        final String SQL_CREATE_NOTE_TABLE = "CREATE TABLE "
                + NotepadContract.NoteEntry.TABLE_NAME + "("
                + NotepadContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NotepadContract.NoteEntry.COLUMN_TITLE + " TEXT NOT NULL,"
                + NotepadContract.NoteEntry.COLUMN_DESCRIPTION + " TEXT"
                + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_NOTE_TABLE);

        sqLiteDatabase.execSQL("INSERT INTO " + NotepadContract.NoteEntry.TABLE_NAME + "("
                + NotepadContract.NoteEntry.COLUMN_TITLE + ","
                + NotepadContract.NoteEntry.COLUMN_DESCRIPTION + ")"
                + "VALUES('Test','Test Description');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
                          int newVersion) {
        // Don't do in a production build!
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NotepadContract.NoteEntry.TABLE_NAME);
    }
}
