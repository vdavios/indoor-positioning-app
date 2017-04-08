package com.bignerdranch.android.IamWatchingYou;


import java.util.ArrayList;
import java.util.List;

public class PointsOfInterest {

    private static PointsOfInterest instance = new PointsOfInterest();
    private List<Position> pointsOfInterest = new ArrayList<>();

    private PointsOfInterest() {
        pointsOfInterest.add(new Position(51.5216929085858,
                -0.12999587740967156, "Vasilis toast"));
        pointsOfInterest.add(new Position(51.5216929085858,
                -0.12999587740967156, "sdas"));
        pointsOfInterest.add(new Position(51.5216929085858,
                -0.12999587740967156, "first "));
    }

    public List<Position> getPointsOfInterest(){
        return pointsOfInterest;
    }

    public static PointsOfInterest getInstance () {
        return  instance;
    }
}


