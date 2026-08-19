# Requirements

## Functional Requirements

1. Register riders (name + location).
2. Register drivers (name + current location + vehicle type).
3. Show available drivers.
4. Request a ride for a registered rider.
5. Match the ride to a driver using a pluggable matching strategy.
6. Calculate the fare for a completed ride using a pluggable pricing strategy.
7. Track ride status across its lifecycle: `REQUESTED`, `ASSIGNED`, `COMPLETED`, `CANCELLED`.

## Non-Functional Requirements

- The pricing algorithm must be easy to extend without editing `RideService` — adding a new fare model should mean adding a new class, not modifying an existing one.
- The driver-matching logic must be easy to swap for the same reason.
- Services should be loosely coupled — each service should depend on interfaces and on the specific collaborator it needs, not reach through one object to get to another.
- The code should be readable and maintainable by someone other than the original author, which is the actual reason this `docs/` folder and the Javadoc on public classes exist.

## Explicitly out of scope (MVP boundary)

Kept out on purpose, in line with YAGNI — none of these were asked for and adding them now would be speculative:

- Persistence (database, file storage). Everything lives in memory for one run.
- A real routing/maps integration. Distance is straight-line, not road distance.
- Concurrency / multiple simultaneous users. This is a single-threaded console app.
- Payments. `FareReceipt` records an amount; it doesn't move any money.
- Ride history beyond what's held in memory during the run (no receipt archive).

## Menu (console, `Main.java`)

```
1. Add Rider
2. Add Driver
3. View Available Drivers
4. Request Ride
5. Complete Ride
6. View Rides
7. Exit
```

Each option is backed only by calls into `RiderService`, `DriverService`, or `RideService` — `Main` has no business logic of its own beyond input parsing and menu control flow.
