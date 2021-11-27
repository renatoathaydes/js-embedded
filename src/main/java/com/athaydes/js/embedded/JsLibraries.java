package com.athaydes.js.embedded;

import java.io.IOException;
import java.util.stream.Stream;

public final class JsLibraries {
    public static final String[] D3_SCALE_FILES = {
            "META-INF/resources/webjars/d3-array/1.2.4/dist/d3-array.min.js",
            "META-INF/resources/webjars/d3-collection/1.0.7/dist/d3-collection.min.js",
            "META-INF/resources/webjars/d3-color/1.4.1/dist/d3-color.min.js",
            "META-INF/resources/webjars/d3-format/1.4.4/dist/d3-format.min.js",
            "META-INF/resources/webjars/d3-interpolate/1.4.0/dist/d3-interpolate.min.js",
            "META-INF/resources/webjars/d3-time/1.1.0/dist/d3-time.min.js",
            "META-INF/resources/webjars/d3-time-format/2.2.3/dist/d3-time-format.min.js",
            "META-INF/resources/webjars/d3-scale/2.2.2/dist/d3-scale.min.js",
    };

    public static final String[] UNDERSCORE_FILES = {
            "META-INF/resources/webjars/underscorejs/1.6.0/underscore-min.js"
    };

    static void verifyAllInClassPath() {
        Stream.of(D3_SCALE_FILES).forEach(JsLibraries::verify);
        Stream.of(UNDERSCORE_FILES).forEach(JsLibraries::verify);
    }

    private static void verify(String library) {
        var lib = JsLibraries.class.getResourceAsStream("/" + library);
        if (lib == null) {
            throw new IllegalStateException("Missing library in classpath: " + library);
        } else {
            try {
                lib.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
