# Setup & Run

## Requirements

- JDK 17+ (developed and tested against JDK 21). Check with:

```
java -version
javac -version
```

No Maven, no Gradle, no third-party libraries — this compiles with the JDK alone.

## Compile

From the project root:

```
find src -name "*.java" > sources.txt
javac -d out @sources.txt
```

This puts compiled `.class` files under `out/`, mirroring the `com/airtribe/ridewise/...` package structure.

## Run

```
java -cp out com.airtribe.ridewise.Main
```

You'll be asked to pick a driver-matching strategy and a fare strategy once, at startup — those choices apply for the rest of that run. After that you're in the main menu:

```
1. Add Rider
2. Add Driver
3. View Available Drivers
4. Request Ride
5. Complete Ride
6. View Rides
7. Exit
```

## Quick manual test

A reasonable smoke test, in order:

1. Add a rider (any name, any x/y coordinates).
2. Add a driver (any name, coordinates, vehicle type).
3. Option 3 — confirm the driver shows up as available.
4. Option 4 — request a ride using the rider's ID (`R1`, printed when you added them). It should auto-assign the driver you just added.
5. Option 3 again — the driver should now be missing from the available list.
6. Option 5 — complete the ride using its ID (`RIDE1`). You should get a fare receipt.
7. Option 6 — the ride should show status `COMPLETED`.
8. Try option 4 or 5 with a made-up ID (e.g. `R99`) — it should print a clean error message instead of crashing.

## Recompiling after a change

Re-run the two commands under **Compile** — there's no incremental build here, `javac` just recompiles everything each time, which is fast enough for a project this size.
