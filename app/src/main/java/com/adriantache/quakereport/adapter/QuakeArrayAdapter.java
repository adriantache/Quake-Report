package com.adriantache.quakereport.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adriantache.quakereport.Earthquake;
import com.adriantache.quakereport.R;

import java.util.List;

/**
 * Custom ArrayAdapter to populate our ListView with our custom list_item layout
 */

public class QuakeArrayAdapter extends ArrayAdapter<Earthquake> {
    public QuakeArrayAdapter(@NonNull Context context, @NonNull List objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.magnitude = convertView.findViewById(R.id.magnitude);
            holder.orientation = convertView.findViewById(R.id.orientation);
            holder.location = convertView.findViewById(R.id.location);
            holder.time = convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Earthquake earthquake = getItem(position);

        if (earthquake != null) {
            holder.magnitude.setText(earthquake.getMagnitude());
            holder.orientation.setText(earthquake.getOrientation());
            holder.location.setText(earthquake.getLocation());
            holder.time.setText(earthquake.getTime());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView magnitude;
        TextView orientation;
        TextView location;
        TextView time;
    }
}
