package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.VehicleType;

/**
 * Flat base fare plus a per-km rate that depends on the vehicle type
 * of the assigned driver.
 */
public class DefaultFareStrategy implements FareStrategy {

    private static final double BASE_FARE = 30.0;

    @Override
    public double calculateFare(Ride ride) {
        double perKmRate = ratePerKm(ride.getDriver().getVehicleType());
        return BASE_FARE + (perKmRate * ride.getDistance());
    }

    private double ratePerKm(VehicleType vehicleType) {
        switch (vehicleType) {
            case BIKE:
                return 6.0;
            case AUTO:
                return 9.0;
            case CAR:
                return 14.0;
            default:
                return 9.0;
        }
    }
}
