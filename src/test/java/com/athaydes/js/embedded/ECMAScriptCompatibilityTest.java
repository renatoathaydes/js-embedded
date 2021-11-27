package com.athaydes.js.embedded;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.athaydes.js.embedded.CustomAsserts.assertNumberBetween;
import static com.athaydes.js.embedded.CustomAsserts.assertNumbersEqual;
import static com.athaydes.js.embedded.CustomAsserts.assertStartsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ECMAScriptCompatibilityTest {
    JsEmbed js = new JsEmbed();

    @Test
    void supportsECMA5UseStrict() {
        var error = Assertions.assertThrows(RuntimeException.class, () -> {
            js.eval("'use strict'\nx=1;");
        });
        assertStartsWith("ReferenceError: \"x\" is not defined", error.getCause().getMessage());
    }

    @Test
    void supportsECMA5ArrayForEach() {
        assertNumbersEqual(6, js.eval("var arr = [1,2,3];\n" +
                "var res = 0;\n" +
                "arr.forEach(function(e) { res += e; });\n" +
                "res"));

    }

    @Test
    void supportsECMA5Json() {
        assertEquals("{\"hello\":true,\"foo\":\"bar\"}",
                js.eval("JSON.stringify({hello: true, foo: \"bar\"})"));
        assertEquals(Map.of("foo", "bar"),
                js.eval("JSON.parse('{\"foo\": \"bar\"}')"));
    }

    @Test
    void supportsECMA5DateNow() {
        var now = System.currentTimeMillis();
        assertNumberBetween(now - 10_000L, now + 10_000L, js.eval("Date.now()"));
    }

    @Test
    void supportsECMA6Let() {
        assertNumbersEqual(30, js.eval("let x = 10; let y = 20; x + y"));
    }

    @Test
    void supportsECMA6Const() {
        assertNumbersEqual(100, js.eval("const z = 40; const w = 60; x + y"));
    }

    @Test
    void supportsECMA6ForOf() {
        assertNumbersEqual(6, js.eval("var arr = [1,2,3];\n" +
                "var res = 0;\n" +
                "for (var a of arr) {\n" +
                "  res += a;\n" +
                "}\n" +
                "res"));
    }

    @Test
    void supportsECMA6SpreadOperator() {
        assertNumbersEqual(5, js.eval("var arr1 = [1,2,3];" +
                "var arr2 = [4,5];" +
                "var arr3 = [...arr1, ...arr2];" +
                "arr3.length"));
    }

    @Test
    void supportsECMA6Destructuring() {
        assertNumbersEqual(2, js.eval("var { re } = { a: 1, re: 2 }; re"));
    }

    @Test
    void supportsECMA6ArrowFunctions() {
        js.eval("plus2 = (x) => x + 2;");
        assertNumbersEqual(2, js.invoke("plus2", 0));
    }

    @Test
    void supportsECMA7ExponentialOperator() {
        assertNumbersEqual(16, js.eval("4 ** 2"));
    }

    @Test
    void supportsECMA7ArrayIncludes() {
        assertEquals(true, js.eval("var arr10 = [1,2,3];\n" +
                "arr10.includes(1)"));
    }

    @Test
    void supportsECMA8StringPadStart() {
        assertEquals("###foo", js.eval("\"foo\".padStart(6, '#')"));
    }

    @Test
    void supportsECMA8AsyncAwait() {
        js.eval("async function asyncFun(x) { return await x(); }");
    }

    @Test
    void supportsECMA9AsyncIteration() {
        js.eval("function asyncIteration(x) {\n" +
                "  for await (var line of x.readLines()) {\n" +
                "    // use line\n" +
                "  }\n" +
                "}");
    }

    @Test
    void supportsECMA9RestParameters() {
        js.eval("function restParameters(a, b, ...c) {}");
    }
}
