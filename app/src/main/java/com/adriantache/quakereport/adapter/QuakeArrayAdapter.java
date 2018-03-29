package com.adriantache.quakereport.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adriantache.quakereport.Earthquake;
import com.adriantache.quakereport.R;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Custom ArrayAdapter to populate our ListView with our custom list_item layout
 */

public class QuakeArrayAdapter extends ArrayAdapter<Earthquake> {
    public QuakeArrayAdapter(@NonNull Context context, @NonNull List<Earthquake> objects) {
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

            GradientDrawable magnitudeCircle = (GradientDrawable) holder.magnitude.getBackground();
            int magnitudeColor = getMagnitudeColor(Double.valueOf(earthquake.getMagnitude()));
            Log.i(TAG, "getView: "+magnitudeColor);
            magnitudeCircle.setColor(magnitudeColor);
        }

        return convertView;
    }

    private int getMagnitudeColor(double magnitude){
        if(magnitude<2) return R.color.magnitude1;
        else if (magnitude<3) return R.color.magnitude2;
        else if (magnitude<4) return R.color.magnitude3;
        else if (magnitude<5) return R.color.magnitude4;
        else if (magnitude<6) return R.color.magnitude5;
        else if (magnitude<7) return R.color.magnitude6;
        else if (magnitude<8) return R.color.magnitude7;
        else if (magnitude<9) return R.color.magnitude8;
        else if (magnitude<10) return R.color.magnitude9;
        else if (magnitude>=10) return R.color.magnitude10plus;
        else return R.color.magnitude1;
    }

    static class ViewHolder {
        TextView magnitude;
        TextView orientation;
        TextView location;
        TextView time;
    }


}
