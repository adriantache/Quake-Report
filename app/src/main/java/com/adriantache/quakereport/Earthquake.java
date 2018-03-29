package com.adriantache.quakereport;

import android.net.Uri;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Custom class to save all relevant earthquake data
 */

public class Earthquake {

    private String magnitude;
    private String orientation;
    private String location;
    private String time;
    private Uri url;

    //todo remove this constructor as we progress
    public Earthquake(double magnitude, String location, long time, String URL) {
        //format the magnitude
        DecimalFormat decimalFormat = new DecimalFormat("0.1");
        this.magnitude = decimalFormat.format(magnitude);

        //extract location String into two lines
        if (location.contains("of ")) {
            this.orientation = location.substring(0, location.indexOf("of") + 2);
            this.location = location.substring(location.indexOf("of") + 3, location.length());
        } else {
            this.orientation = "Near the";
            this.location = location;
        }

        //convert time from UNIX time to a String
        Date date = new Date(time);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        // use the timezone from the USGS
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        this.time = sdf.format(date);
        this.time = this.time.replace(" ", "\n");

        //convert string to URI to make sure it's correct
        this.url = Uri.parse(URL);
    }

    public String getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public String getOrientation() {
        return orientation;
    }

    public String getTime() {
        return time;
    }

    public Uri getUrl() {
        return url;
    }
}
