package com.bignerdranch.android.IamWatchingYou;


import java.lang.Math;
import java.util.Set;
import java.util.TreeSet;


public class PositionContainer {
    private static final PositionContainer ourInstance = new PositionContainer();
    private Set<Position> pointsOfInterest;
    public static PositionContainer getInstance() {
        return ourInstance;
    }
    private static int earthRadiusKilometers = 6371009;
    private int thresholdDistance = 3;

    private PositionContainer() {
        pointsOfInterest = new TreeSet<Position>() {{
            add(new Position(12312, 1231231,"1stPointOfInterest"));
            add(new Position(12312, 1231231,"2ndPointOfInterest"));
            add(new Position(12312, 1231231,"3rdPointOfInterest"));
        }};
    }

    private boolean areTheyCloseEnough(Position position, double longitude,
                                      double latitude) {
        double meanLatitude = (latitude + position.getLatitude()) / 2;
        double deltaLatitude = latitude - position.getLatitude();
        double deltaLongitude = longitude - position.getLatitude();

        double sqrtArgs = Math.pow(deltaLatitude, 2) +
                Math.pow((Math.cos(meanLatitude) * deltaLongitude), 2);

        double distance = earthRadiusKilometers *
                Math.sqrt(sqrtArgs);

        return distance < thresholdDistance;
    }

    public String isNearPointsOfInterest(double longitude, double latitude) {
        double latitudeRad = Math.toRadians(latitude);
        double longitudeRad = Math.toRadians(longitude);
        for(Position position : pointsOfInterest) {
            if (areTheyCloseEnough(position, latitudeRad, longitudeRad)){
                return position.getDescription();
            }
        }
        return "";
    }
}