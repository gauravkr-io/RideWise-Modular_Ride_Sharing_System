package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;

import java.util.List;

/**
 * Picks which driver should handle a rider's request. New matching
 * algorithms are added by writing another implementation of this
 * interface - RideService never needs to change (OCP).
 */
public interface RideMatchingStrategy {

    Driver findDriver(Rider rider, List<Driver> drivers);
}
