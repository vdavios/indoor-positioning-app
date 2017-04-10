package com.bignerdranch.android.IamWatchingYou;


import java.util.ArrayList;
import java.util.List;

public class PointsOfInterest {

    private static PointsOfInterest instance = new PointsOfInterest();
    private List<Position> pointsOfInterest = new ArrayList<>();

    /**
     * Stores our list of points of interest.
     *
     */
    private PointsOfInterest() {
        pointsOfInterest.add(new Position(51.5218130200724,
                -0.13019382823249614, "Main Lobby-Elevator"));
        pointsOfInterest.add(new Position(51.52192292986247,
                -0.13039357668297738, "Library and Uni-cafeteria corridor"));
        pointsOfInterest.add(new Position(51.522128,
                -0.130617, "Entrance to extension building."));
    }

    public List<Position> getPointsOfInterest(){
        return pointsOfInterest;
    }

    public static PointsOfInterest getInstance () {
        return  instance;
    }
}


