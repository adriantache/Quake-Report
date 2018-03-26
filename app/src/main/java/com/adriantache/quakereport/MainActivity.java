package com.adriantache.quakereport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.adriantache.quakereport.adapter.QuakeArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a fake list of earthquake locations.
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        earthquakes.add(new Earthquake(7.2f,"San Francisco","Feb 2, 2016"));
        earthquakes.add(new Earthquake(6.1f,"London","Feb 2, 2016"));
        earthquakes.add(new Earthquake(3.9f,"Tokyo","Feb 2, 2016"));
        earthquakes.add(new Earthquake(4.5f,"Mexico City","Feb 2, 2016"));
        earthquakes.add(new Earthquake(2.8f,"Moscow","Feb 2, 2016"));
        earthquakes.add(new Earthquake(1.6f,"Rio de Janeiro","Feb 2, 2016"));
        earthquakes.add(new Earthquake(1.6f,"Paris","Feb 2, 2016"));

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.listView);

        // Create a new {@link ArrayAdapter} of earthquakes
        QuakeArrayAdapter adapter = new QuakeArrayAdapter(this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
    }
}
