package com.athaydes.js.embedded;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CustomAsserts {
    public static void assertNumbersEqual(Number expected, Object actual) {
        if (actual instanceof Number) {
            actual = ((Number) actual).doubleValue();
        }
        assertEquals(expected.doubleValue(), actual);
    }

    public static void assertNumberBetween(double min, double max, Object actual) {
        if (actual instanceof Number) {
            double actualDouble = ((Number) actual).doubleValue();
            if (min <= actualDouble && actualDouble <= max) {
                return;
            }
        }
        fail("Not a number between " + min + " and " + max + ": " + actual);
    }

    public static void assertStartsWith(String expectedPrefix, Object actual) {
        if (actual instanceof String) {
            var actualString = (String) actual;
            if (actualString.startsWith(expectedPrefix)) {
                return;
            }
        }
        fail("'" + actual + "' does not start with '" + expectedPrefix + "'");
    }
}
