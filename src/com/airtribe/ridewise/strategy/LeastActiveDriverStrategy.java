package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;

import java.util.List;

/**
 * Assigns the available driver with the fewest completed rides so far,
 * so work stays spread out instead of always going to the same drivers.
 */
public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        Driver leastActive = null;
        int lowestRideCount = Integer.MAX_VALUE;

        for (Driver driver : drivers) {
            if (!driver.isAvailable()) {
                continue;
            }
            if (driver.getCompletedRideCount() < lowestRideCount) {
                lowestRideCount = driver.getCompletedRideCount();
                leastActive = driver;
            }
        }

        if (leastActive == null) {
            throw new NoDriverAvailableException("No available driver found for rider " + rider.getName());
        }
        return leastActive;
    }
}
