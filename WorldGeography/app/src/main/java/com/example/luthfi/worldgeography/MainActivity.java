package com.example.luthfi.worldgeography;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AbsListView.MultiChoiceModeListener {

    private ArrayList<String> countries;
    private ArrayList<String> selected;
    private ArrayList<String> fakes;

    private ArrayAdapter<String> adapter;

    private Toolbar toolbar;
    private ListView listView;


    private void stringSwap(ArrayList<String> arr, int pos1, int pos2) {
        String temp = arr.get(pos1);
        arr.set(pos1, arr.get(pos2));
        arr.set(pos2, temp);
    }

    private void quickSortRange(ArrayList<String> arr, int start, int end) {
        if (end <= start) return;

        String pivot = arr.get(start);
        int pos = start + 1;

        for (int i = start + 1; i <= end; ++i) {
            if (arr.get(i).compareTo(pivot) < 0) {
                stringSwap(arr, i, pos);
                ++pos;
            }
        }

        --pos;
        stringSwap(arr, start, pos);

        quickSortRange(arr, start, pos - 1);
        quickSortRange(arr, pos + 1, end);
    }

    private void quickSort(ArrayList<String> arr) {
        quickSortRange(arr, 0, arr.size() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countries = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.countries_array)));
        selected = new ArrayList<String>();
        fakes = new ArrayList<String>();
        countries.addAll(Arrays.asList(getResources().getStringArray(R.array.fake_countries_array)));
        fakes.addAll(Arrays.asList(getResources().getStringArray(R.array.fake_countries_array)));
        quickSort(countries);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, countries);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        } else if (id == R.id.action_refresh) {
            countries.clear();
            countries.addAll(Arrays.asList(getResources().getStringArray(R.array.countries_array)));
            countries.addAll(Arrays.asList(getResources().getStringArray(R.array.fake_countries_array)));
            fakes.addAll(Arrays.asList(getResources().getStringArray(R.array.fake_countries_array)));
            quickSort(countries);
            adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (checked) {
            selected.add(adapter.getItem(position));
        } else {
            selected.remove(adapter.getItem(position));
        }

        mode.setTitle(String.valueOf(selected.size()));
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cab, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                for (String country : selected) {
                    if (fakes.contains(country)) {
                        fakes.remove(country);
                    }
                    countries.remove(country);
                }
                adapter.notifyDataSetChanged();
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        Toast.makeText(this, "Removed " + selected.size() + " items", Toast.LENGTH_SHORT).show();

        if (fakes.isEmpty()) {
            Toast.makeText(this, "Congratulations! You have removed all the fake countries!",
                    Toast.LENGTH_LONG).show();
        }

        selected.clear();
    }
}
