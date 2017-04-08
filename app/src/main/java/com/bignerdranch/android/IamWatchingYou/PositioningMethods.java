package com.bignerdranch.android.IamWatchingYou;


import android.util.Log;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;



public class PositioningMethods {
    private static final  String TAG = "oeo";
    private static PointsOfInterest pointsOfInterest = PointsOfInterest.getInstance();
    private boolean toastFlag = true;
    private static PositioningMethods ourInstance = new PositioningMethods();

    private PositioningMethods() {

    }

    public static PositioningMethods getInstance() {
        return ourInstance;
    }

    public boolean getToastFlag() {
        return toastFlag;
    }

    public void setToastFlag(boolean flagValue) {
        toastFlag = flagValue;
    }

    private boolean areTheyCloseEnough(Position position, double latitude,
                                      double longitude) {
        final int earthRadiusInMeters = 6371009;
        final int thresholdDistance = 3;
     //   double distance = 0 ;
      //  Log.d(TAG, "latitude = " + latitude + " getLatitude = " + position.getLatitude());
        double meanLatitude = (latitude + position.getLatitude()) / 2;
        double deltaLatitude = (latitude - position.getLatitude());
        double deltaLongitude = (longitude - position.getLongitude());

        double sqrtArgs = Math.pow(deltaLatitude, 2) +
                Math.pow((Math.cos(meanLatitude) * deltaLongitude), 2);
       // Log.d(TAG, "Args = "+Math.sqrt(sqrtArgs));
        double distance = earthRadiusInMeters *
                Math.sqrt(sqrtArgs);

         Log.d(TAG, "distance = " + distance);
        return distance < thresholdDistance ;

    }

    public  List<Position> isNearPointsOfInterest(double latitude, double longitude) {
        List<Position> pointsOfInterestNearby = new ArrayList<>();
        double latitudeRad = Math.toRadians(latitude);
        double longitudeRad = Math.toRadians(longitude);
        for(Position position: pointsOfInterest.getPointsOfInterest()) {
            if (areTheyCloseEnough(position, latitudeRad, longitudeRad)){
                pointsOfInterestNearby.add(position);
            }
        }
        if (pointsOfInterestNearby.size() == 0) {
            setToastFlag(true);
            Log.d(TAG, String.valueOf(toastFlag));
        }
        return pointsOfInterestNearby;
    }
}