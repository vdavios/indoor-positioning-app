package com.bignerdranch.android.IamWatchingYou;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Objects of this class are going to be saved in our db.
 */

public class Records {
    private double latitude;
    private double longitude;
    private long time;
    //private FirebaseDatabase myRef;

    public Records(double latitude, double longitude, long time){

    //}FirebaseDatabase myRef) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
     //   this.myRef = myRef;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTime() {
        return time;
    }
}
