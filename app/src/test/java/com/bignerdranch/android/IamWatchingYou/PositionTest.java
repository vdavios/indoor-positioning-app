package com.bignerdranch.android.IamWatchingYou;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vasileiosDavios on 05/04/2017.
 */
public class PositionTest {
    Position usersPosition;
    PositioningMethods positioningMethods = PositioningMethods.getInstance();
    @Before
    public void setUp() {
        usersPosition = new Position(51.52170448542311, -0.1300068345539113, "Pos");
    }

    @Test
    public void getLatitude() {
        assertEquals(Math.toRadians(51.52170448542311), usersPosition.getLatitude(), 0);
    }

    @Test
    public void getLongitude(){
        assertEquals(Math.toRadians(-0.1300068345539113), usersPosition.getLongitude(), 0);
    }

    @Test
    public void assesDistanceIsComputedCorrectly() {
        assertEquals();
    }


}