package com.airtribe.ridewise.factory;

import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.LeastActiveDriverStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import com.airtribe.ridewise.strategy.PeakHourFareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;

/**
 * Turns a menu choice into the right strategy object. Keeps the
 * "which class do I new up for option 2" logic out of Main, so Main
 * only ever talks to interfaces.
 */
public final class StrategyFactory {

    private StrategyFactory() {
    }

    public static RideMatchingStrategy createMatchingStrategy(int choice) {
        switch (choice) {
            case 1:
                return new NearestDriverStrategy();
            case 2:
                return new LeastActiveDriverStrategy();
            default:
                throw new IllegalArgumentException("Unknown matching strategy option: " + choice);
        }
    }

    public static FareStrategy createFareStrategy(int choice) {
        switch (choice) {
            case 1:
                return new DefaultFareStrategy();
            case 2:
                return new PeakHourFareStrategy(new DefaultFareStrategy());
            default:
                throw new IllegalArgumentException("Unknown fare strategy option: " + choice);
        }
    }
}
