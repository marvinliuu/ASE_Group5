package com.example.testdisasterevent.algorithms;

import android.annotation.TargetApi;
import android.os.Build;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FindMaxAreaAlgorithm {
    @TargetApi(Build.VERSION_CODES.N)
    public List<LatLng> findMaxAreaPoints(List<LatLng> points, int k) {
        if (points == null || points.size() < k) {
            return new ArrayList<>();
        }

        points.sort(Comparator.comparingDouble(p -> p.latitude));
        double maxArea = 0;
        List<LatLng> result = new ArrayList<>();

        for (int i = 0; i <= points.size() - k; i++) {
            List<LatLng> currentPoints = points.subList(i, i + k);
            double area = calculateArea(currentPoints);
            if (area > maxArea) {
                maxArea = area;
                result = currentPoints;
            }
        }
        return result;
    }

    private static double calculateArea(List<LatLng> points) {
        double area = 0;
        int j = points.size() - 1;
        for (int i = 0; i < points.size(); i++) {
            area += (points.get(j).longitude + points.get(i).longitude) * (points.get(j).latitude - points.get(i).latitude);
            j = i;
        }
        return Math.abs(area / 2);
    }
}
