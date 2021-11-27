package com.athaydes.js.embedded;

import org.junit.jupiter.api.Assertions;

public class CustomAsserts {
    public static void assertNumbersEqual(Number expected, Object actual) {
        if (actual instanceof Number) {
            actual = ((Number) actual).doubleValue();
        }
        Assertions.assertEquals(expected.doubleValue(), actual);
    }
}
