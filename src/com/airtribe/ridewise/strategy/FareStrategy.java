package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

/**
 * Computes the fare for a completed ride. New pricing schemes are added
 * by implementing this interface - RideService just calls
 * calculateFare() and doesn't care how the number is produced (OCP/DIP).
 */
public interface FareStrategy {

    double calculateFare(Ride ride);
}
