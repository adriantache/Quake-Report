package com.adriantache.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adriantache.quakereport.adapter.QuakeArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>>, SwipeRefreshLayout.OnRefreshListener {

    private static final String USGS_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=20";
    private static final String TAG = "MainActivity";
    private ListView earthquakeListView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //test network connectivity first
        ConnectivityManager cm = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = findViewById(R.id.listView);

        // set click listener to open quake detail page
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake earthquake = (Earthquake) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(Intent.ACTION_VIEW, earthquake.getUrl());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        //set empty view for when we get no results
        TextView emptyView = findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyView);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        //run app only if connectivity is detected
        if (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {
            //run Loader to populate earthquakes array
            getSupportLoaderManager().initLoader(0, null, this);
        } else {
            hideProgressBar();
            emptyView.setText(R.string.no_internet);
        }
    }

    //process JSON string and populate quakes ArrayList
    public List<Earthquake> extractQuakes(String strJson) {
        List<Earthquake> quakes = new ArrayList<>();

        try {
            JSONObject jsonRootObject = new JSONObject(strJson);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("features");

            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject properties = jsonObject.getJSONObject("properties");
                quakes.add(new Earthquake(properties.getDouble("mag"), properties.getString("place"), properties.getLong("time"), properties.getString("url")));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
        }

        return quakes;
    }

    private void setUpList(List<Earthquake> quake) {
        // Create a new {@link ArrayAdapter} of earthquakes
        final QuakeArrayAdapter adapter = new QuakeArrayAdapter(this, quake);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
        hideProgressBar();
    }

    private void hideProgressBar() {
        ProgressBar indeterminateBar = findViewById(R.id.indeterminateBar);
        indeterminateBar.setVisibility(View.GONE);

    }

    //loader callbacks methods
    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String minMagnitude = sharedPrefs.getString(
                "min_magnitude",
                "0");

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(USGS_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");

        // Return the completed uri `http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=10&minmag=minMagnitude&orderby=time
        return new EarthquakeLoader(this, this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> quake) {
        hideProgressBar();

        if (quake.size() == 0) {
            TextView emptyView = findViewById(R.id.empty_view);
            emptyView.setText(R.string.no_quakes);
        } else {
            //activate the adapter to populate the list
            setUpList(quake);
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        setUpList(new ArrayList<Earthquake>());

        swipeRefreshLayout.setRefreshing(false);
    }

    //menu related methods
    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRefresh() {
        getSupportLoaderManager().restartLoader(0, null, this).forceLoad();
    }
}