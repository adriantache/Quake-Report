package com.adriantache.quakereport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Custom class to save all relevant earthquake data
 */

public class Earthquake {

    private double magnitude;
    private String orientation;
    private String location;
    private String time;

    public Earthquake(double magnitude, String orientation, String location, long time) {
        this.magnitude = magnitude;
        this.orientation = orientation;
        this.location = location;
        //convert time from UNIX time to a String
        Date date = new Date(time);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        // use the timezone from the USGS
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        this.time = sdf.format(date);
    }

    //todo remove this constructor as we progress
    public Earthquake(double magnitude, String location, long time) {
        this.magnitude = magnitude;
        this.location = location;
        //convert time from UNIX time to a String
        Date date = new Date(time);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        // use the timezone from the USGS
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        this.time = sdf.format(date);
    }

    public double getMagnitude() {
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
}
