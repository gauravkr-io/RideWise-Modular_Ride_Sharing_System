package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.config.PeakHourConfig;
import com.airtribe.ridewise.model.Ride;

/**
 * Wraps another FareStrategy and multiplies its result during peak
 * hours. Built as composition rather than inheritance - it holds a
 * reference to the strategy it's boosting instead of extending it, so
 * any base strategy can be made "peak aware" without duplicating fare
 * math.
 */
public class PeakHourFareStrategy implements FareStrategy {

    private final FareStrategy baseStrategy;
    private final PeakHourConfig peakHourConfig;

    public PeakHourFareStrategy(FareStrategy baseStrategy) {
        this(baseStrategy, PeakHourConfig.getInstance());
    }

    public PeakHourFareStrategy(FareStrategy baseStrategy, PeakHourConfig peakHourConfig) {
        this.baseStrategy = baseStrategy;
        this.peakHourConfig = peakHourConfig;
    }

    @Override
    public double calculateFare(Ride ride) {
        double baseFare = baseStrategy.calculateFare(ride);
        if (peakHourConfig.isPeakNow()) {
            return baseFare * peakHourConfig.getPeakMultiplier();
        }
        return baseFare;
    }
}
