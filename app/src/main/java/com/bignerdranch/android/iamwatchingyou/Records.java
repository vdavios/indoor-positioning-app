package com.bignerdranch.android.iamwatchingyou;

/**
 * Objects of this class are going to be saved in our db.
 */

public class Records {
    private Position usersCurrentPosition;
    private long time;
    public Records(Position position, long time) {
        usersCurrentPosition = position;
        this.time = time;
    }
}
