package com.airtribe.ridewise;

import com.airtribe.ridewise.exception.RideWiseException;
import com.airtribe.ridewise.factory.StrategyFactory;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;

import java.util.List;
import java.util.Scanner;

/**
 * Console entry point. Everything here goes through the service layer -
 * this class never touches a Map, a strategy implementation, or a
 * model's internals directly.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final RiderService riderService = new RiderService();
    private static final DriverService driverService = new DriverService();
    private static RideService rideService;

    public static void main(String[] args) {
        System.out.println("=== RideWise: Modular Ride-Sharing System ===");
        rideService = buildRideService();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    addRider();
                    break;
                case 2:
                    addDriver();
                    break;
                case 3:
                    viewAvailableDrivers();
                    break;
                case 4:
                    requestRide();
                    break;
                case 5:
                    completeRide();
                    break;
                case 6:
                    viewRides();
                    break;
                case 7:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option, please choose between 1 and 7.");
            }
        }
        scanner.close();
    }

    private static RideService buildRideService() {
        System.out.println("\nSelect a driver matching strategy:");
        System.out.println("1. Nearest Driver");
        System.out.println("2. Least Active Driver");
        int matchChoice = readInt("Choice: ");

        System.out.println("\nSelect a fare strategy:");
        System.out.println("1. Default Fare");
        System.out.println("2. Peak Hour Fare");
        int fareChoice = readInt("Choice: ");

        return new RideService(
                StrategyFactory.createMatchingStrategy(matchChoice),
                StrategyFactory.createFareStrategy(fareChoice),
                riderService,
                driverService
        );
    }

    private static void printMenu() {
        System.out.println("\n----- Main Menu -----");
        System.out.println("1. Add Rider");
        System.out.println("2. Add Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Request Ride");
        System.out.println("5. Complete Ride");
        System.out.println("6. View Rides");
        System.out.println("7. Exit");
    }

    private static void addRider() {
        try {
            System.out.print("Rider name: ");
            String name = scanner.nextLine();
            Location location = readLocation();
            Rider rider = riderService.registerRider(name, location);
            System.out.println("Registered: " + rider);
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add rider: " + e.getMessage());
        }
    }

    private static void addDriver() {
        try {
            System.out.print("Driver name: ");
            String name = scanner.nextLine();
            Location location = readLocation();
            VehicleType vehicleType = readVehicleType();
            Driver driver = driverService.registerDriver(name, location, vehicleType);
            System.out.println("Registered: " + driver);
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add driver: " + e.getMessage());
        }
    }

    private static void viewAvailableDrivers() {
        List<Driver> drivers = driverService.getAvailableDrivers();
        if (drivers.isEmpty()) {
            System.out.println("No drivers are currently available.");
            return;
        }
        for (Driver driver : drivers) {
            System.out.println(driver);
        }
    }

    private static void requestRide() {
        try {
            System.out.print("Rider ID: ");
            String riderId = scanner.nextLine().trim();
            Ride ride = rideService.requestRide(riderId);
            System.out.println("Ride assigned: " + ride);
        } catch (RideWiseException e) {
            System.out.println("Could not request ride: " + e.getMessage());
        }
    }

    private static void completeRide() {
        try {
            System.out.print("Ride ID: ");
            String rideId = scanner.nextLine().trim();
            FareReceipt receipt = rideService.completeRide(rideId);
            System.out.println("Ride completed. " + receipt);
        } catch (RideWiseException e) {
            System.out.println("Could not complete ride: " + e.getMessage());
        }
    }

    private static void viewRides() {
        List<Ride> rides = rideService.getAllRides();
        if (rides.isEmpty()) {
            System.out.println("No rides yet.");
            return;
        }
        for (Ride ride : rides) {
            System.out.println(ride);
        }
    }

    private static Location readLocation() {
        double x = readDouble("  X coordinate: ");
        double y = readDouble("  Y coordinate: ");
        return new Location(x, y);
    }

    private static VehicleType readVehicleType() {
        System.out.println("  Vehicle type: 1. BIKE  2. AUTO  3. CAR");
        int choice = readInt("  Choice: ");
        switch (choice) {
            case 1:
                return VehicleType.BIKE;
            case 2:
                return VehicleType.AUTO;
            case 3:
                return VehicleType.CAR;
            default:
                System.out.println("  Invalid choice, defaulting to AUTO.");
                return VehicleType.AUTO;
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
