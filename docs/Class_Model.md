# Class Model

## Package layout

```
com.airtribe.ridewise
├── Main.java                     entry point / console menu
├── model/                        plain data holders
│   ├── Rider.java
│   ├── Driver.java
│   ├── Ride.java
│   ├── FareReceipt.java
│   ├── RideStatus.java           enum
│   ├── VehicleType.java          enum
│   └── Location.java             x/y coordinate + distance calc
├── strategy/                     the two Strategy-pattern hierarchies
│   ├── RideMatchingStrategy.java (interface)
│   ├── NearestDriverStrategy.java
│   ├── LeastActiveDriverStrategy.java
│   ├── FareStrategy.java         (interface)
│   ├── DefaultFareStrategy.java
│   └── PeakHourFareStrategy.java
├── service/                      business logic, one class per concern
│   ├── RiderService.java
│   ├── DriverService.java
│   └── RideService.java
├── exception/                    unchecked, domain-specific failures
│   ├── RideWiseException.java    (abstract base)
│   ├── RiderNotFoundException.java
│   ├── DriverNotFoundException.java
│   ├── RideNotFoundException.java
│   ├── NoDriverAvailableException.java
│   └── InvalidRideStateException.java
├── util/
│   └── IdGenerator.java          shared id generation (R1, D1, RIDE1, ...)
├── config/
│   └── PeakHourConfig.java       singleton: peak-hour windows + multiplier
└── factory/
    └── StrategyFactory.java      menu choice -> strategy instance
```

## Class responsibilities

**Rider** — id, name, `Location`. Immutable once created.

**Driver** — id, name, `Location` (mutable — a driver can move), `available` flag, `VehicleType`, and a running count of completed rides (read by `LeastActiveDriverStrategy`).

**Ride** — id, the `Rider` who requested it, the `Driver` assigned to it (null until matched), `distance`, `RideStatus`, and timestamps. This is the object every strategy and service ultimately reads from or writes to.

**FareReceipt** — a value object: ride id, amount, timestamp. Produced once, at ride completion, and handed back to the caller.

**RiderService / DriverService** — each owns one in-memory map and exposes register/lookup/list behavior for exactly one entity type. Neither knows the other exists.

**RideService** — the only class that touches all three model relationships (Rider, Driver, Ride) at once, because ride creation is inherently the point where a rider and a driver become linked. It depends on `RiderService`, `DriverService`, `RideMatchingStrategy`, and `FareStrategy` — all supplied through its constructor, all as interfaces or already-built collaborators, never constructed internally.

**RideMatchingStrategy / FareStrategy** — the two extension points. Everything that varies (how a driver is picked, how a fare is priced) is isolated behind these two interfaces.

**StrategyFactory** — the one class allowed to `new` a concrete strategy. Everything else refers to strategies only by interface type.

**PeakHourConfig** — holds the two peak-hour windows and the multiplier as shared, read-only state; `PeakHourFareStrategy` reads from it instead of hardcoding those numbers itself.

**IdGenerator** — one shared utility for producing sequential prefixed ids, used by all three services instead of each keeping a private counter.

## Key relationships

- `Ride` **has-a** `Rider` and **has-a** `Driver` — a `Ride` cannot exist without a `Rider` (constructor requires one); the `Driver` reference starts null and is filled in once matched.
- `RideService` **has-a** `RideMatchingStrategy` and a `FareStrategy` — composition, injected through the constructor, swappable at construction time.
- `PeakHourFareStrategy` **has-a** `FareStrategy` (the strategy it wraps) — composition over inheritance: it boosts another strategy's result instead of subclassing it.
- See `Object_Relationships.md` for the association-vs-composition classification of each pair.
