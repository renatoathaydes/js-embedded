package com.athaydes.js.embedded;

import org.junit.jupiter.api.Test;

import static com.athaydes.js.embedded.CustomAsserts.assertNumbersEqual;

public class JSLibrariesTest {
    JsEmbed js = new JsEmbed();

    @Test
    void canUseD3ScaleLibrary() {
        js.eval("var d3 = loadWithNewGlobal('classpath:" +
                "META-INF/resources/webjars/d3-scale/4.0.2/dist/d3-scale.min.js');");

        // example usage from https://github.com/d3/d3-scale
        js.eval("var x = d3.scaleLinear()\n" +
                "    .domain([10, 130])\n" +
                "    .range([0, 960]);\n");

        assertNumbersEqual(80, js.eval("x(20)"));
        assertNumbersEqual(320, js.eval("x(50)"));
    }

}
