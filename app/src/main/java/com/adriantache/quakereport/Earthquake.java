package com.adriantache.quakereport;

/**
 * Custom class to save all relevant earthquake data
 */

public class Earthquake {

    private float magnitude;
    private String orientation;
    private String location;
    private String time;

    public Earthquake(float magnitude, String orientation, String location, String time) {
        this.magnitude = magnitude;
        this.orientation = orientation;
        this.location = location;
        this.time = time;
    }

    //todo remove this constructor as we progress
    public Earthquake(float magnitude, String location, String time) {
        this.magnitude = magnitude;
        this.location = location;
        this.time = time;
    }

    public float getMagnitude() {
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
