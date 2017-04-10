package com.bignerdranch.android.IamWatchingYou;

import org.junit.Before;
import org.junit.Test;



import static org.junit.Assert.*;


public class PositionTest {
    private Position usersPosition;

    @Before
    public void setUp() {
        usersPosition = new Position(51.52170448542311, -0.1300068345539113, "Pos");
    }


    @Test
    public void getLatitudeTest() {
        assertEquals(Math.toRadians(51.52170448542311), usersPosition.getLatitude(), 0);
    }

    @Test
    public void getLongitudeTest() {
        assertEquals(Math.toRadians(-0.1300068345539113), usersPosition.getLongitude(), 0);
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("Pos", usersPosition.getDescription());

    }







}