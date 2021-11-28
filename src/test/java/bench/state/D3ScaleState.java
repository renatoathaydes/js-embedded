package bench.state;

import com.athaydes.js.embedded.JsEmbed;
import com.athaydes.js.embedded.JsLibraries;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class D3ScaleState {

    private static final String TEST_FUNCTIONS = "" +
            "function fun0(n) {\n" +
            "  var x = d3.scaleLinear()\n" +
            "    .domain([10, 130])\n" +
            "    .range([0, 960]);\n" +
            "  return x(n);" +
            "}\n" +
            "function fun1(n) {\n" +
            "  var color = d3.scaleQuantize()\n" +
            "    .domain([0, 1])\n" +
            "    .range([\"brown\", \"steelblue\"]);\n" +
            "  return color(n);\n" +
            "}\n" +
            "function fun2(n) {\n" +
            "  var color = d3.scaleThreshold()\n" +
            "    .domain([0, 1])\n" +
            "    .range([\"red\", \"white\", \"green\"]);\n" +
            "  return color(n);\n" +
            "}\n";

    public final JsEmbed js = new JsEmbed();

    @Param({"0", "1", "2"})
    public int funIndex;

    private Double n;

    @Setup(Level.Trial)
    public void setUpBenchmark() {
        js.load(JsLibraries.D3_SCALE_FILES);
        js.eval(TEST_FUNCTIONS);
    }

    @Setup(Level.Iteration)
    public void setUpIteration() {
        var rangeMin = funIndex == 0 ? 10.0 : 0.0;
        var rangeWidth = funIndex == 0 ? 120.0 : 1.0;
        n = Math.random() * rangeWidth + rangeMin;
    }

    public Object runScript() {
        return js.invoke("fun" + funIndex, n);
    }

    public static void main(String[] args) {
        System.out.println("----------------------------------");
        System.out.println("Setting up d3-scale benchmark");
        var state = new D3ScaleState();

        var startTime = System.currentTimeMillis();
        state.setUpBenchmark();
        var loadTime = System.currentTimeMillis() - startTime;

        final int scriptCount = 3;
        long[] funTimes = new long[scriptCount];

        for (int i = 0; i < scriptCount; i++) {
            System.out.println("Running fun" + i);
            state.funIndex = i;
            state.setUpIteration();
            startTime = System.currentTimeMillis();
            var result = state.runScript();
            funTimes[i] = System.currentTimeMillis() - startTime;
            System.out.println("==================================");
            System.out.println("fun" + i + "(" + state.n + ") ---> " + result);
            System.out.println("----------------------------------");
        }

        System.out.println("Results:\n" +
                "    Load time: " + loadTime + "ms");

        for (int i = 0; i < scriptCount; i++) {
            System.out.println("    fun" + i + " time: " + funTimes[i] + "ms");
        }
    }

}
