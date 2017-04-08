package com.bignerdranch.android.IamWatchingYou;



public class Position {

    private double latitude;
    private double longitude;
    private String description;

    public Position(double latitude,
                    double longitude, String description) {
        this.latitude = Math.toRadians(latitude);
        this.longitude = Math.toRadians(longitude);
        this.description = description;
    }

    //Created for saving separately in our database
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }
}