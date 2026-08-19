package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;

import java.util.List;

/**
 * Assigns the closest available driver, by straight-line distance from
 * the rider's registered location.
 */
public class NearestDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        Driver nearest = null;
        double shortestDistance = Double.MAX_VALUE;

        for (Driver driver : drivers) {
            if (!driver.isAvailable()) {
                continue;
            }
            double distance = rider.getLocation().distanceTo(driver.getCurrentLocation());
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearest = driver;
            }
        }

        if (nearest == null) {
            throw new NoDriverAvailableException("No available driver found near rider " + rider.getName());
        }
        return nearest;
    }
}
