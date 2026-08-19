package com.airtribe.ridewise.exception;

/**
 * Base type for every domain-specific error RideWise can throw. Kept
 * unchecked (extends RuntimeException) since the console layer catches
 * this common base at each menu option rather than forcing throws
 * declarations through every service method.
 */
public abstract class RideWiseException extends RuntimeException {

    protected RideWiseException(String message) {
        super(message);
    }
}
