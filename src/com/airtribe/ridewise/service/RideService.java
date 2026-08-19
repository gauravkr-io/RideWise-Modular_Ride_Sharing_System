package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.InvalidRideStateException;
import com.airtribe.ridewise.exception.RideNotFoundException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Coordinates the ride lifecycle: request -> match -> assign,
 * assign -> complete -> fare, and assign/request -> cancel.
 *
 * Both the matching algorithm and the pricing algorithm are handed in
 * through the constructor rather than hardcoded here. That's what lets
 * either one change (or get swapped at runtime) without touching this
 * class - this is the DIP/OCP part of the design the brief asks for.
 */
public class RideService {

    private static final String ID_PREFIX = "RIDE";

    private final RideMatchingStrategy matchingStrategy;
    private final FareStrategy fareStrategy;
    private final RiderService riderService;
    private final DriverService driverService;
    private final Map<String, Ride> rides = new LinkedHashMap<>();

    public RideService(RideMatchingStrategy matchingStrategy,
                        FareStrategy fareStrategy,
                        RiderService riderService,
                        DriverService driverService) {
        this.matchingStrategy = matchingStrategy;
        this.fareStrategy = fareStrategy;
        this.riderService = riderService;
        this.driverService = driverService;
    }

    public Ride requestRide(String riderId) {
        Rider rider = riderService.getRiderById(riderId);

        String rideId = IdGenerator.nextId(ID_PREFIX);
        Ride ride = new Ride(rideId, rider);
        rides.put(rideId, ride);

        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        Driver assignedDriver = matchingStrategy.findDriver(rider, availableDrivers);

        // Distance is derived from the same Location data the matching
        // strategy already used, instead of asking the user to type it
        // in separately - one source of truth, no duplicated input.
        double distance = rider.getLocation().distanceTo(assignedDriver.getCurrentLocation());
        ride.setDistance(distance);
        ride.setDriver(assignedDriver);
        ride.setStatus(RideStatus.ASSIGNED);
        assignedDriver.setAvailable(false);

        return ride;
    }

    public FareReceipt completeRide(String rideId) {
        Ride ride = getRideById(rideId);

        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new InvalidRideStateException(
                    "Ride " + rideId + " cannot be completed from status " + ride.getStatus());
        }

        double fare = fareStrategy.calculateFare(ride);

        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());

        Driver driver = ride.getDriver();
        driver.setAvailable(true);
        driver.incrementCompletedRideCount();

        return new FareReceipt(rideId, fare);
    }

    public Ride cancelRide(String rideId) {
        Ride ride = getRideById(rideId);

        if (ride.getStatus() == RideStatus.COMPLETED || ride.getStatus() == RideStatus.CANCELLED) {
            throw new InvalidRideStateException(
                    "Ride " + rideId + " cannot be cancelled from status " + ride.getStatus());
        }

        if (ride.getDriver() != null) {
            ride.getDriver().setAvailable(true);
        }
        ride.setStatus(RideStatus.CANCELLED);
        return ride;
    }

    public Ride getRideById(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            throw new RideNotFoundException(rideId);
        }
        return ride;
    }

    public List<Ride> getAllRides() {
        return new ArrayList<>(rides.values());
    }
}
