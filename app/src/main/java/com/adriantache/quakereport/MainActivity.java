package com.adriantache.quakereport;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adriantache.quakereport.adapter.QuakeArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String USGS_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private ArrayList<Earthquake> earthquakes;
    private ListView earthquakeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a list of earthquake locations.
        earthquakes = new ArrayList<>();

        //run AsyncTask to populate earthquakes array
        new EarthquakeAsyncTask().execute(USGS_URL);

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
    }

    //process JSON string and populate quakes ArrayList
    private void extractQuakes(String strJson) {
        try {
            JSONObject jsonRootObject = new JSONObject(strJson);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("features");

            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject properties = jsonObject.getJSONObject("properties");
                earthquakes.add(new Earthquake(properties.getDouble("mag"), properties.getString("place"), properties.getLong("time"), properties.getString("url")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUpList(){
        // Create a new {@link ArrayAdapter} of earthquakes
        final QuakeArrayAdapter adapter = new QuakeArrayAdapter(this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (TextUtils.isEmpty(strings[0])) return null;

            return Utils.makeHTTPConnection(strings[0]);
        }

        @Override
        protected void onPostExecute(String strJson) {
            if (strJson == null) return;

            extractQuakes(strJson);

           setUpList();
        }
    }

}
