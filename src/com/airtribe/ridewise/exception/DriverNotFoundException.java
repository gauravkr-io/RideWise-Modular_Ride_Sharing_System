package com.airtribe.ridewise.exception;

public class DriverNotFoundException extends RideWiseException {

    public DriverNotFoundException(String driverId) {
        super("No driver found with id: " + driverId);
    }
}
