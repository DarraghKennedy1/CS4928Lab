package com.cafepos.domain;

public final class OrderIds {
    private static long nextId = 1000;

    private OrderIds() {
        // Utility class - prevent instantiation
    }

    public static long next() {
        return ++nextId;
    }
}
