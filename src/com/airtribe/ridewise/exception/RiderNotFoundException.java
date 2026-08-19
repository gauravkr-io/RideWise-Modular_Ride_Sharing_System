package com.airtribe.ridewise.exception;

public class RiderNotFoundException extends RideWiseException {

    public RiderNotFoundException(String riderId) {
        super("No rider found with id: " + riderId);
    }
}
