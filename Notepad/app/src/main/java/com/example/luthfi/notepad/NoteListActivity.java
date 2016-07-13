package com.example.luthfi.notepad;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.luthfi.notepad.data.NotepadContract;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AbsListView.MultiChoiceModeListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private static final int NOTES_LOADER = 1;
    private static final String[] PROJECTION = {
            NotepadContract.NoteEntry._ID,
            NotepadContract.NoteEntry.COLUMN_TITLE};
    private SimpleCursorAdapter adapter;

    ArrayList<Long> selectedIDs = new ArrayList<Long>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        ListView listView = (ListView) findViewById(R.id.listView);
        String[] from = new String[] { NotepadContract.NoteEntry.COLUMN_TITLE };
        int[] to = new int[] { android.R.id.text1 };
        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_activated_1, null, from, to, 0);
        listView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(NOTES_LOADER, null, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case NOTES_LOADER:
                return new CursorLoader(this, NotepadContract.NoteEntry.CONTENT_URI,
                        PROJECTION, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int position,
                                          long id, boolean checked) {
        if (checked) selectedIDs.add(id);
        else selectedIDs.remove(id);
        actionMode.setTitle(String.valueOf(selectedIDs.size()));
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cab, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    // Delete selected note.
    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_delete:
                for (long id : selectedIDs) {
                    getContentResolver().delete(
                            ContentUris.withAppendedId(NotepadContract.NoteEntry.CONTENT_URI, id),
                            null,
                            null
                    );
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        selectedIDs.clear();
    }

    // Creates a new note upon clicking the Floating Action Button.
    @Override
    public void onClick(View view) {
        // Creates a new Intent and associates it to this activity.
        Intent intent = new Intent(this, NoteEditActivity.class);
        // Set intent action to 'creating a new note'.
        intent.setAction(NoteEditActivity.ACTION_NEW);
        // Initializes the activity.
        startActivity(intent);
    }

    // Edit existing notes.
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        // Creates a new Intent and associates it tot his activity.
        Intent intent = new Intent(this, NoteEditActivity.class);
        // Assign the ID of the current selected note.
        intent.setData(ContentUris.withAppendedId(NotepadContract.NoteEntry.CONTENT_URI, id));
        // Set intent action to 'edit this note'.
        intent.setAction(NoteEditActivity.ACTION_EDIT);
        // Initializes the activity.
        startActivity(intent);
    }
}
