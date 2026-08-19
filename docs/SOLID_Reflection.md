# SOLID Reflection

Short writeup on where each principle actually shows up in this codebase, rather than just asserting "SOLID was followed."

## Single Responsibility Principle

`RiderService`, `DriverService`, and `RideService` each have exactly one reason to change: rider-related rules, driver-related rules, or ride-lifecycle rules, respectively. Before I settled on three services I considered one `RideService` doing everything (register a rider, register a driver, request a ride), and the problem was obvious once I imagined adding a rule like "riders need email validation" — that change would sit inside a class that also handles fare calculation and driver matching, for no reason. Splitting them means a rider-validation change touches exactly one file.

The same logic applies to the exception hierarchy — `RiderNotFoundException` and `RideNotFoundException` are separate types instead of one `NotFoundException` with a "what kind" field, so the two failure modes stay independently catchable.

## Open/Closed Principle

`RideService` is closed for modification with respect to both matching logic and pricing logic — it only ever calls `matchingStrategy.findDriver(...)` and `fareStrategy.calculateFare(...)`. Adding `LeastActiveDriverStrategy` after `NearestDriverStrategy` already existed required zero changes to `RideService`; the same will be true for whatever strategy comes next. That's the actual test of OCP here, not just "we used an interface" — the fact that the second implementation didn't touch the first one or the class that uses them.

## Liskov Substitution Principle

Both `NearestDriverStrategy` and `LeastActiveDriverStrategy` honor the same contract: given a rider and a list of drivers, either return an available driver or throw `NoDriverAvailableException` — never return null, never return an unavailable driver. `RideService` doesn't null-check the result, which only works because both implementations are held to that same contract. Same story for `FareStrategy` — both implementations return a non-negative `double` and never mutate the `Ride` passed in.

## Interface Segregation Principle

`RideMatchingStrategy` has exactly one method. `FareStrategy` has exactly one method. Neither forces an implementer to provide behavior it doesn't need (there was a version I considered where `FareStrategy` also had a `getName()` for display purposes — dropped it, because `PeakHourFareStrategy` wrapping another `FareStrategy` would have had to decide what its "name" even means, for a method nothing actually calls).

## Dependency Inversion Principle

`RideService` depends on the `RideMatchingStrategy` and `FareStrategy` interfaces, not on `NearestDriverStrategy` or `DefaultFareStrategy` directly — those concrete types are only named in `StrategyFactory`, which exists specifically to be the one place that knows about them. `Main` depends on `RiderService`/`DriverService`/`RideService` as concrete classes (there was no interface asked for at the service layer, and introducing `IRideService` with a single implementation would be interface-for-its-own-sake), but never on any concrete strategy class.

## Where I made a judgment call rather than a strict rule

`Main` still directly constructs `RiderService` and `DriverService` — full DIP would mean even those are injected from somewhere above `Main`, but there's nothing above `Main`; it's the composition root. Wiring dependencies at the composition root and injecting everywhere else is the normal way DIP is applied in a program without a framework doing the wiring for you.
