package com.airtribe.ridewise.model;

import java.time.LocalDateTime;

public class Ride {

    private final String id;
    private final Rider rider;
    private Driver driver;
    private double distance;
    private RideStatus status;
    private final LocalDateTime requestedAt;
    private LocalDateTime completedAt;

    public Ride(String id, Rider rider) {
        this.id = id;
        this.rider = rider;
        this.status = RideStatus.REQUESTED;
        this.requestedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public String toString() {
        String driverName = (driver == null) ? "unassigned" : driver.getName();
        return String.format("Ride{id='%s', rider='%s', driver='%s', distance=%.2fkm, status=%s}",
                id, rider.getName(), driverName, distance, status);
    }
}
