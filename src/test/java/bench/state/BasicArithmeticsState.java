package bench.state;

import com.athaydes.js.embedded.JsEmbed;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
public class BasicArithmeticsState {

    public final JsEmbed js = new JsEmbed();

    @Param({"100", "200", "300"})
    public int n;

    public String script;

    @Setup(Level.Iteration)
    public void setUp() {
        script = "2 + " + n + " * 10";
    }

    public Object runScript() {
        return js.eval(script);
    }

    public static void main(String[] args) {
        System.out.println("----------------------------------");
        System.out.println("Setting up basic-arithmetics benchmark");
        var state = new BasicArithmeticsState();

        final int scriptCount = 3;
        int[] params = {100, 200, 300};
        long[] funTimes = new long[scriptCount];

        for (int i = 0; i < scriptCount; i++) {
            System.out.println("Running fun" + i);
            state.n = params[i];
            state.setUp();
            var startTime = System.currentTimeMillis();
            var result = state.runScript();
            funTimes[i] = System.currentTimeMillis() - startTime;
            System.out.println("==================================");
            System.out.println(state.script + " ---> " + result);
            System.out.println("----------------------------------");
        }

        System.out.println("Results:\n");

        for (int i = 0; i < scriptCount; i++) {
            System.out.println("    param" + i + " time: " + funTimes[i] + "ms");
        }
    }
}
