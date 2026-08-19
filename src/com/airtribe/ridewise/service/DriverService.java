package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.DriverNotFoundException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Owns driver registration, availability, and lookup.
 */
public class DriverService {

    private static final String ID_PREFIX = "D";

    private final Map<String, Driver> drivers = new LinkedHashMap<>();

    public Driver registerDriver(String name, Location location, VehicleType vehicleType) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Driver name cannot be empty");
        }
        if (location == null) {
            throw new IllegalArgumentException("Driver location cannot be null");
        }
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        String id = IdGenerator.nextId(ID_PREFIX);
        Driver driver = new Driver(id, name.trim(), location, vehicleType);
        drivers.put(id, driver);
        return driver;
    }

    public Driver getDriverById(String driverId) {
        Driver driver = drivers.get(driverId);
        if (driver == null) {
            throw new DriverNotFoundException(driverId);
        }
        return driver;
    }

    public void updateAvailability(String driverId, boolean available) {
        Driver driver = getDriverById(driverId);
        driver.setAvailable(available);
    }

    public List<Driver> getAvailableDrivers() {
        List<Driver> available = new ArrayList<>();
        for (Driver driver : drivers.values()) {
            if (driver.isAvailable()) {
                available.add(driver);
            }
        }
        return available;
    }

    public List<Driver> getAllDrivers() {
        return new ArrayList<>(drivers.values());
    }
}
