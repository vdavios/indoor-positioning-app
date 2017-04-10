package com.bignerdranch.android.IamWatchingYou;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static junit.framework.Assert.assertEquals;


public class PositionMethodsTest {
    PositioningMethods positioningMethods;
    private Position usersPosition;
    @Before
    public void setUp() {
        positioningMethods = PositioningMethods.getInstance();
        usersPosition = new Position(51.52170448542311, -0.1300068345539113, "Pos");

    }

    @Test
    public void testFlag() {
        positioningMethods.setToastFlag(false);
        assertEquals(false, positioningMethods.getToastFlag());
    }

    @Test
    public void assertPointsOfInterestAreFound() {
        Assert.assertEquals(positioningMethods.isNearPointsOfInterest(51.521815,
                -0.130188, 3).size(), 1, 0);

    }

    @Test
    public void assertPointsOfInterestNotFound() {
        Assert.assertEquals(positioningMethods.isNearPointsOfInterest(usersPosition.getLatitude(),
                usersPosition.getLongitude(), 3).size(), 0, 0);
    }


}
