package com.example.testdisasterevent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.example.testdisasterevent.algorithms.FindMaxAreaAlgorithm;
import com.google.android.gms.maps.model.LatLng;

public class FindMaxAreaAlgorithmTest {

    @Test
    public void testFindMaxAreaPointsCorrect() {
        List<LatLng> points = new ArrayList<>(Arrays.asList(
                new LatLng(10, 20),
                new LatLng(20, 10),
                new LatLng(30, 40),
                new LatLng(40, 30),
                new LatLng(50, 20),
                new LatLng(60, 10),
                new LatLng(70, 20),
                new LatLng(80, 30)
        ));
        int k = 3;
        List<LatLng> expectedPoints = Arrays.asList(
                new LatLng(10, 20),
                new LatLng(20, 10),
                new LatLng(30, 40)
        );

        List<LatLng> actualPoints = new FindMaxAreaAlgorithm().findMaxAreaPoints(points, k);

        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testFindMaxAreaPointsInCorrect() {
        List<LatLng> points = new ArrayList<>(Arrays.asList(
                new LatLng(10, 20),
                new LatLng(20, 10),
                new LatLng(30, 40),
                new LatLng(40, 30),
                new LatLng(50, 20),
                new LatLng(60, 10),
                new LatLng(70, 20),
                new LatLng(80, 30)
        ));
        int k = 3;
        List<LatLng> expectedPoints = Arrays.asList(
                new LatLng(60, 20),
                new LatLng(20, 10),
                new LatLng(30, 40)
        );

        List<LatLng> actualPoints = new FindMaxAreaAlgorithm().findMaxAreaPoints(points, k);

        assertNotEquals(expectedPoints, actualPoints);
    }
}
