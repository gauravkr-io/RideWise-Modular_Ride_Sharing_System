package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.RiderNotFoundException;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Owns rider registration and lookup. Nothing outside this class
 * touches the rider storage directly.
 */
public class RiderService {

    private static final String ID_PREFIX = "R";

    private final Map<String, Rider> riders = new LinkedHashMap<>();

    public Rider registerRider(String name, Location location) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Rider name cannot be empty");
        }
        if (location == null) {
            throw new IllegalArgumentException("Rider location cannot be null");
        }
        String id = IdGenerator.nextId(ID_PREFIX);
        Rider rider = new Rider(id, name.trim(), location);
        riders.put(id, rider);
        return rider;
    }

    public Rider getRiderById(String riderId) {
        Rider rider = riders.get(riderId);
        if (rider == null) {
            throw new RiderNotFoundException(riderId);
        }
        return rider;
    }

    public List<Rider> getAllRiders() {
        return new ArrayList<>(riders.values());
    }
}
