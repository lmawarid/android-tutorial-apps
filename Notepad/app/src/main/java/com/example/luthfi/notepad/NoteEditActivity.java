package com.example.luthfi.notepad;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.luthfi.notepad.data.NotepadContract;

/**
 * Created by Luthfi on 11/7/2016.
 */
public class NoteEditActivity extends AppCompatActivity {

    public static final String ACTION_NEW = "action_new";
    public static final String ACTION_EDIT = "action_edit";

    private Uri note;

    private EditText titleEditText;
    private EditText descriptionEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        final Intent intent = getIntent();

        if (intent.getAction().equals(ACTION_NEW)) {
            // Create a blank note and insert it into the database.
            ContentValues initialValues = new ContentValues();
            initialValues.put(NotepadContract.NoteEntry.COLUMN_TITLE, "");
            initialValues.put(NotepadContract.NoteEntry.COLUMN_DESCRIPTION, "");
            // Set the current note to be this newly-created note.
            note = getContentResolver().insert(NotepadContract.NoteEntry.CONTENT_URI, initialValues);
        } else if (intent.getAction().equals(ACTION_EDIT)) {
            // Set the current note to be the selected note.
            note = intent.getData();
        }

        // Initialize the views.
        titleEditText = (EditText) findViewById(R.id.edittext_title);
        descriptionEditText = (EditText) findViewById(R.id.edittext_description);

        // Initialize cursor, get note text, and close.
        Cursor cursor = getContentResolver().query(note, null, null, null, null);
        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndexOrThrow(NotepadContract.NoteEntry.COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(NotepadContract.NoteEntry.COLUMN_DESCRIPTION));
        cursor.close();

        // Set note title and text to each EditText.
        titleEditText.setText(title);
        descriptionEditText.setText(description);

        // Initialize the toolbar for this view.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Note");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Finish this activity and move back to the previous activity on the
            // call stack.
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    // Save our data.
    private void save() {
        ContentValues values = new ContentValues();
        values.put(NotepadContract.NoteEntry.COLUMN_TITLE, titleEditText.getText().toString());
        values.put(NotepadContract.NoteEntry.COLUMN_DESCRIPTION,
                descriptionEditText.getText().toString());
        getContentResolver().update(note, values, null, null);
    }
}
