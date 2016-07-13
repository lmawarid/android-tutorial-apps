package com.example.luthfi.androidweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_REQUEST = 1;

    // Create Google API client and ForecastAdapter.
    private GoogleApiClient googleApiClient;
    private ForecastAdapter adapter;
    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the app toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);

        // Initialize forecast adapter and assign this adapter to our ListView.
        adapter = new ForecastAdapter(this, R.layout.list_item);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Initialize the Google API client.
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // Connect to the Google API client on starting the app.
    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    // On exiting the app, disconnect from the Google API client if it is still
    // connected.
    @Override
    protected void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
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
        }

        return super.onOptionsItemSelected(item);
    }

    // Handles user response on giving/denying permission.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST:
                // If permission is granted, carry on.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                // Otherwise, try to request permission again.
                // TODO: Show user why the permission is necessary.
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST);
                }
                break;
        }
    }

    // Call upon connection with the Google API client.
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // Request user permission to access their location.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST);
        }

        // Initialize the user's last location.
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        // If lastLocation is not null, retrieve forecasts for that location by
        // initializing a new RetrieveForecastTask and then executing it with the
        // location's latitude and longitude.
        if (lastLocation != null) {
            RetrieveForecastTask task = new RetrieveForecastTask(adapter);
            task.execute(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        return;
    }
}
