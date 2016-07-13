package com.example.luthfi.notepad.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Luthfi on 9/7/2016.
 */
public final class NotepadContract {
    private NotepadContract() {

    }

    public static final String AUTHORITY = "com.example.luthfi.notepad.data.NotepadProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_NOTE = "notes";

    public static final class NoteEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().
                appendPath(PATH_NOTE).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" +
                AUTHORITY + "/" + PATH_NOTE;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" +
                AUTHORITY + "/" + PATH_NOTE;

        public static final String TABLE_NAME = "note";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
    }
}
