package bench;

import com.athaydes.js.embedded.JsEmbed;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
public class Benchmarks {

    JsEmbed js = new JsEmbed();

    @Benchmark
    @Warmup(iterations = 0)
    @Fork(value = 2, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    public void coldStartTrivial() {
        js.eval("2 + 4 * 10");
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}
