package com.airtribe.ridewise.exception;

public class RideNotFoundException extends RideWiseException {

    public RideNotFoundException(String rideId) {
        super("No ride found with id: " + rideId);
    }
}
