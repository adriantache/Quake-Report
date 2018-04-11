package com.adriantache.quakereport;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.adriantache.quakereport.adapter.QuakeArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final String USGS_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=20";
    private static final String TAG = "MainActivity";
    private ListView earthquakeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //run Loader to populate earthquakes array
        getSupportLoaderManager().initLoader(0, null, this);

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
    }

    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        return new EarthquakeLoader(this, MainActivity.this, USGS_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> quake) {
        if (quake.size() == 0) {
            TextView emptyView = findViewById(R.id.empty_view);
            emptyView.setText("No earthquakes found.");
        } else {
            //activate the adapter to populate the list
            setUpList(quake);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        setUpList(new ArrayList<Earthquake>());
    }
}
