# RideWise — Modular Ride-Sharing System

A small console-based ride-sharing simulator written in plain Java, built as a low-level design exercise around SOLID principles and the strategy pattern. No frameworks, no database — everything lives in memory for the duration of one run.

You register riders and drivers, request a ride, and the system picks a driver using whichever matching strategy you chose at startup, then prices the ride using whichever fare strategy you chose. The point of the exercise isn't the ride-sharing domain itself, it's that swapping how a driver gets picked or how a fare gets calculated shouldn't require touching `RideService`.

## Features

- Register riders and drivers, each with a location on a simple x/y grid
- View which drivers are currently available
- Request a ride — matched to a driver automatically, distance computed from rider/driver locations
- Complete a ride — generates a fare receipt
- Track ride status through `REQUESTED → ASSIGNED → COMPLETED` (or `CANCELLED`)
- Two interchangeable driver-matching strategies and two interchangeable fare strategies, picked at startup

## Project layout

```
src/com/ridewise/
  model/        Rider, Driver, Ride, FareReceipt, Location, RideStatus, VehicleType
  exception/    RideWiseException and its subtypes
  strategy/
    matching/   RideMatchingStrategy + NearestDriverStrategy, LeastActiveDriverStrategy
    fare/       FareStrategy + DefaultFareStrategy, PeakHourFareStrategy
  config/       PeakHourConfig (singleton holding peak-hour windows/multiplier)
  factory/      StrategyFactory (turns a menu choice into a strategy instance)
  service/      RiderService, DriverService, RideService
  app/          Main — the console menu
```

## How the design maps to the brief

**Strategy pattern for matching and pricing.** `RideMatchingStrategy` and `FareStrategy` are the only two interfaces `RideService` knows about. It's handed concrete implementations through its constructor (`NearestDriverStrategy` or `LeastActiveDriverStrategy`, `DefaultFareStrategy` or `PeakHourFareStrategy`), so adding a third matching algorithm or pricing scheme later means writing one new class, not editing `RideService`.

**Composition over inheritance.** `PeakHourFareStrategy` doesn't extend `DefaultFareStrategy` — it wraps a `FareStrategy` and multiplies its result during peak hours. That means it works on top of *any* base strategy, not just the default one.

**Single-purpose services.** `RiderService`, `DriverService`, and `RideService` each own one slice of state and don't reach into each other's internals — `RideService` asks `RiderService`/`DriverService` for a `Rider`/`Driver` object and works with that, it never touches their storage maps.

**Custom exceptions instead of null checks scattered everywhere.** Things like "rider doesn't exist" or "this ride isn't in a state where it can be completed" are modeled as exception types under `exception/`, all extending a common `RideWiseException` so `Main` can catch one type per menu action and print a message instead of crashing.

**Factory for strategy selection.** `StrategyFactory` is the one place that knows "menu choice 1 means `NearestDriverStrategy`." That logic lives outside `Main` and outside `RideService`, so neither has to know about concrete strategy classes.

**Singleton for shared config.** `PeakHourConfig` holds the peak-hour time windows and multiplier. There's only one sensible set of these values for the whole app, so it's a singleton rather than something each `PeakHourFareStrategy` instance builds for itself.

### A couple of judgment calls worth flagging

- **Ride distance isn't typed in by the user.** It's computed from the rider's and the assigned driver's `Location` — the same data `NearestDriverStrategy` already uses. Asking the user to also type in a distance would just be a second, possibly-inconsistent source of truth for the same trip.
- **`RideService.cancelRide()` exists but isn't wired into the console menu.** The brief's menu only lists seven options and none of them is "cancel," but `RideStatus.CANCELLED` is a required state, so the state transition is implemented and can be called directly or exposed as an eighth menu option later without changing `RideService`.
- **Vehicle type affects the fare.** `VehicleType` (BIKE/AUTO/CAR) is attached to `Driver` and used by `DefaultFareStrategy` to pick a per-km rate — otherwise the enum would exist but never actually influence anything.

## Requirements

- JDK 17 or newer (built and tested on JDK 21). No Maven, no Gradle, no external libraries.

See `SETUP.md` for exact build/run commands.

## Known limitations

- Everything is in-memory, data is gone when the program exits.
- Single-threaded console app — no concurrent ride requests to worry about.
- No persistence layer, no REST API — out of scope for this assignment.
