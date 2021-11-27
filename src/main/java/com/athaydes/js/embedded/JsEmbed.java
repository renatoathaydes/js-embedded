package com.athaydes.js.embedded;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;

public class JsEmbed {
    private final ScriptEngine engine = new ScriptEngineManager()
            .getEngineByName("nashorn");

    public JsEmbed() {
        // verify that D3 JS library is in the classpath
        var d3 = getClass().getResourceAsStream(
                "/META-INF/resources/webjars/d3-scale/4.0.2/dist/d3-scale.min.js");
        if (d3 == null) {
            System.out.println("ERROR: d3 is not in the classpath!");
        } else {
            try {
                d3.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
