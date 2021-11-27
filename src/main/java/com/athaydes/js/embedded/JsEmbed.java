package com.athaydes.js.embedded;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JsEmbed {
    private final ScriptEngine engine = new ScriptEngineManager()
            .getEngineByName("nashorn");

    public Object eval(String script) {
        try {
            return engine.eval(script);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public Object invoke(String functionName, Object... args) {
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

    public static void main(String[] args) {
        var result = new JsEmbed().eval("2 + 3");

        if (result instanceof Number) {
            System.out.println("Got number: " + result);
        } else {
            System.out.println("Unexpected result: " + result);
        }
    }
}
