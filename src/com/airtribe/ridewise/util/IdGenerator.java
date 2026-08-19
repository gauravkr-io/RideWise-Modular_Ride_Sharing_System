package com.airtribe.ridewise.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * One place that knows how to turn "give me a rider id" into "R7". Used
 * by all three services instead of each keeping its own copy of the same
 * prefix + counter logic.
 */
public final class IdGenerator {

    private static final Map<String, AtomicInteger> COUNTERS = new ConcurrentHashMap<>();

    private IdGenerator() {
    }

    public static String nextId(String prefix) {
        AtomicInteger counter = COUNTERS.computeIfAbsent(prefix, key -> new AtomicInteger(0));
        return prefix + counter.incrementAndGet();
    }
}
