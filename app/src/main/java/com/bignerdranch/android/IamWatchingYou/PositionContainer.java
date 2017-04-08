package com.bignerdranch.android.IamWatchingYou;


import android.util.Log;

import java.lang.Math;
import java.util.LinkedHashSet;
import java.util.Set;



public class PositionContainer {
    private static final  String TAG = "oeo";
    private Position p1;
    private Set<Position> pointsOfInterest;
    private static PositionContainer ourInstance = new PositionContainer();

    private PositionContainer() {
        p1 = new Position(51.521683120556155, -0.12999050799063985, "firstPosition");
        pointsOfInterest = new LinkedHashSet<>();
        pointsOfInterest.add(p1);
    }

    public static PositionContainer getInstance() {
        if(ourInstance != null) {
            return ourInstance;
        }
        return null;
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

    public  String isNearPointsOfInterest(double latitude, double longitude) {
        String toReturn = "";
        double latitudeRad = Math.toRadians(latitude);
        double longitudeRad = Math.toRadians(longitude);
        for(Position position : pointsOfInterest) {
            if (areTheyCloseEnough(position, latitudeRad, longitudeRad)){
                toReturn = position.getDescription();
                break;

            }
        }
        return toReturn;
    }
}