package com.athaydes.js.embedded;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static java.util.Objects.requireNonNull;

public final class JsEmbed {

    private final ScriptEngine engine = new ScriptEngineManager()
            .getEngineByName("nashorn");

    public JsEmbed() {
        requireNonNull(engine, "Nashorn engine is not available");
        JsLibraries.verifyAllInClassPath();
        eval("function __toJava__(obj) {\n" +
                "  return Java.asJSONCompatible(obj);\n" +
                " }");
    }

    public Object eval(String script) {
        try {
            return convertToJava(engine.eval(script));
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public Object invoke(String functionName, Object... args) {
        return convertToJava(invokeWithoutTypeConversion(functionName, args));
    }

    public void load(String... libraries) {
        for (String lib : libraries) {
            eval("load('classpath:" + lib + "');");
        }
    }

    private Object invokeWithoutTypeConversion(String functionName, Object... args) {
        if (engine instanceof Invocable) {
            try {
                return ((Invocable) engine).invokeFunction(functionName, args);
            } catch (ScriptException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalStateException("ScriptEngine is not Invocable");
        }
    }

    @SuppressWarnings("removal")
    private Object convertToJava(Object jsObject) {
        if (jsObject instanceof ScriptObjectMirror) {
            return invokeWithoutTypeConversion("__toJava__", jsObject);
        }
        return jsObject;
    }

    public static void main(String[] args) {
        var script = "2 + 3";
        System.out.println("Executing JS script:\n" +
                "==========================\n" +
                script + "\n" +
                "--------------------------");

        var result = new JsEmbed().eval(script);

        if (result instanceof Number) {
            System.out.println("Got number: " + result);
        } else {
            System.out.println("Unexpected result: " + result);
        }
    }
}
