package com.airtribe.ridewise.config;

import java.time.LocalTime;

/**
 * Shared, read-only configuration for what counts as "peak hours" and
 * by how much the fare is boosted during them. There's only ever one
 * meaningful set of peak-hour rules for the whole app, so this is a
 * plain Singleton rather than something each strategy builds on its own.
 */
public final class PeakHourConfig {

    private static PeakHourConfig instance;

    private final LocalTime morningPeakStart = LocalTime.of(8, 0);
    private final LocalTime morningPeakEnd = LocalTime.of(10, 30);
    private final LocalTime eveningPeakStart = LocalTime.of(17, 30);
    private final LocalTime eveningPeakEnd = LocalTime.of(21, 0);
    private final double peakMultiplier = 1.5;

    private PeakHourConfig() {
    }

    public static synchronized PeakHourConfig getInstance() {
        if (instance == null) {
            instance = new PeakHourConfig();
        }
        return instance;
    }

    public boolean isPeakNow() {
        return isPeak(LocalTime.now());
    }

    public boolean isPeak(LocalTime time) {
        boolean inMorningWindow = !time.isBefore(morningPeakStart) && !time.isAfter(morningPeakEnd);
        boolean inEveningWindow = !time.isBefore(eveningPeakStart) && !time.isAfter(eveningPeakEnd);
        return inMorningWindow || inEveningWindow;
    }

    public double getPeakMultiplier() {
        return peakMultiplier;
    }
}
