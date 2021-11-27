package com.athaydes.js.embedded;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.athaydes.js.embedded.CustomAsserts.assertNumbersEqual;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JsEmbedTest {
    JsEmbed js = new JsEmbed();

    @Test
    public void canReturnNull() {
        assertNull(js.eval("null"));
    }

    @Test
    public void undefinedIsReturnedAsNull() {
        assertNull(js.eval("undefined"));
    }

    @Test
    public void canReturnPrimitiveValues() {
        assertNumbersEqual(0, js.eval("0"));
        assertNumbersEqual(42, js.eval("42"));
        assertNumbersEqual(3.14159, js.eval("3.14159"));
        assertEquals(true, js.eval("true"));
        assertEquals(false, js.eval("false"));
    }

    @Test
    public void canReturnJavaString() {
        assertEquals("hello JS", js.eval("\"hello JS\""));
    }

    @Test
    public void canReturnJavaListOfPrimitives() {
        assertEquals(List.of(1, 2, true), js.eval("[1, 2, true]"));
    }

    @Test
    public void canReturnJavaListOfStringsEquals() {
        assertEquals(List.of("foo", "bar"), js.eval("[\"foo\", \"bar\"]"));
    }

    @Test
    public void canReturnJavaListOfStrings() {
        var list = (List<?>) js.eval("[\"foo\", \"bar\"]");
        assertEquals("foo", list.get(0));
        assertEquals("bar", list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    public void canReturnJavaListOfListsEquals() {
        assertEquals(List.of(List.of(), List.of("a", 2, false)),
                js.eval("[[],[\"a\",2,false]]"));
    }

    @Test
    public void canReturnJavaMapEquals() {
        assertEquals(Map.of(
                "hello", "world",
                "nested", Map.of("foo", true)
        ), js.eval("map = {\n" +
                "  \"hello\": \"world\",\n" +
                "  \"nested\": { \"foo\": true }\n" +
                "};"));
    }

    @Test
    public void canReturnJavaMap() {
        var map = (Map<?, ?>) js.eval("map = {\n" +
                "  \"hello\": \"world\",\n" +
                "  \"nested\": { \"foo\": true }\n" +
                "};");
        assertEquals("world", map.get("hello"));

        var nested = (Map<?, ?>) map.get("nested");
        assertEquals(true, nested.get("foo"));

        assertEquals(2, map.size());
        assertEquals(1, nested.size());
    }

    @Test
    public void canDoBasicArithmetics() {
        assertNumbersEqual(5, js.eval("2 + 3"));
        assertNumbersEqual(14, js.eval("5 * 2 + 4"));
    }

    @Test
    public void javaCanInvokeJsFunctionNoArgs() {
        js.eval("function magic() { return 42 }");
        assertNumbersEqual(42, js.invoke("magic"));
    }

    @Test
    public void javaCanInvokeJsFunctionWithPrimitiveArgs() {
        js.eval("function add(a, b) { return a + b }\n" +
                "function not(s) { return !s }");
        assertNumbersEqual(42, js.invoke("add", 40, 2));
        assertNumbersEqual(0.5, js.invoke("add", 0.25, 0.25));
        assertNumbersEqual(-0.5, js.invoke("add", 0.25, -0.75));
        assertEquals(true, js.invoke("not", false));
        assertEquals(false, js.invoke("not", true));
    }

    @Test
    public void canApplyIndexOnJavaList() {
        js.eval("function getFirst(list) { return list[0] }");
        assertNumbersEqual(42, js.invoke("getFirst", List.of(42, 24)));
    }

    @Test
    public void canPushItemIntoJavaArrayList() {
        js.eval("function push(list) { list.push(42) }");
        var list = new ArrayList<>(2);
        js.invoke("push", list);
        assertEquals(List.of(42), list);
    }

    @Test
    public void canAddItemToJavaArrayList() {
        js.eval("function addTo(list) { list.add(42) }");
        var list = new ArrayList<>(2);
        js.invoke("addTo", list);
        assertEquals(List.of(42), list);
    }

    @Test
    public void canApplyIndexOnJavaMap() {
        js.eval("function get(map) { return map[\"foo\"] }");
        assertEquals("bar", js.invoke("get", Map.of("foo", "bar")));
    }

    @Test
    void canPutItemByIndexingJavaHashMap() {
        js.eval("function put(map) { map[\"js\"] = true }");
        var map = new HashMap<>(2);
        map.put("java", true);
        js.invoke("put", map);
        assertEquals(Map.of("java", true, "js", true), map);
    }

    @Test
    void canPutItemIntoJavaHashMap() {
        js.eval("function put(map) { map.put(\"js\", true) }");
        var map = new HashMap<>(2);
        map.put("java", true);
        js.invoke("put", map);
        assertEquals(Map.of("java", true, "js", true), map);
    }

    @Test
    public void canCallPojoGetters() {
        var pojo = new Pojo("joe", 30);
        js.eval("function name(pojo) { return pojo.getName() }\n" +
                "function number(pojo) { return pojo.getNumber() }");
        assertEquals("joe", js.invoke("name", pojo));
        assertNumbersEqual(30, js.invoke("number", pojo));
    }

    @Test
    public void canCallPojoGettersUsingPropertySyntax() {
        var pojo = new Pojo("joe", 30);
        js.eval("function name(pojo) { return pojo.name }\n" +
                "function number(pojo) { return pojo.number }");
        assertEquals("joe", js.invoke("name", pojo));
        assertNumbersEqual(30, js.invoke("number", pojo));
    }

    @Test
    public void canCallPojoSetters() {
        var pojo = new Pojo("joe", 30);
        js.eval("function setName(pojo) { pojo.setName(\"mary\") }\n" +
                "function setNumber(pojo) { return pojo.setNumber(40) }");
        js.invoke("setName", pojo);
        assertEquals("mary", pojo.getName());
        js.invoke("setNumber", pojo);
        assertEquals(40, pojo.getNumber());
    }

    @Test
    public void canCallPojoSettersUsingPropertySyntax() {
        var pojo = new Pojo("joe", 30);
        js.eval("function setName(pojo) { pojo.name = \"mary\" }\n" +
                "function setNumber(pojo) { return pojo.number = 40 }");
        js.invoke("setName", pojo);
        assertEquals("mary", pojo.getName());
        js.invoke("setNumber", pojo);
        assertEquals(40, pojo.getNumber());
    }

    @Test
    void canCreatePojo() {
        var jsPojo = js.eval("var Pojo = Java.type(\"com.athaydes.js.embedded.Pojo\");\n" +
                "new Pojo(\"jsPojo\", 11)");
        assertEquals(new Pojo("jsPojo", 11), jsPojo);
    }

}
