package com.athaydes.js.embedded;

import org.junit.jupiter.api.Test;

import static com.athaydes.js.embedded.CustomAsserts.assertNumbersEqual;

public class JSLibrariesTest {

    JsEmbed js = new JsEmbed();

    @Test
    void canUseD3ScaleLibrary() {
        js.load(JsLibraries.D3_SCALE_FILES);

        // example usage from https://github.com/d3/d3-scale
        js.eval("var x = d3.scaleLinear()\n" +
                "    .domain([10, 130])\n" +
                "    .range([0, 960]);\n");

        assertNumbersEqual(80, js.eval("x(20)"));
        assertNumbersEqual(320, js.eval("x(50)"));
    }

    @Test
    void canUseUnderscoreJsLibrary() {
        js.load(JsLibraries.UNDERSCORE_FILES);

        // example based on https://winterbe.com/posts/2014/04/05/java8-nashorn-tutorial/#loading-scripts
        assertNumbersEqual(9, js.eval(
                "var odds = _.filter([1, 2, 3, 4, 5, 6], function (num) {\n" +
                        "    return num % 2 == 1;\n" +
                        "});\n" +
                        "_.reduce(odds, function(a, b) { return a + b; }, 0)"));
    }

}
