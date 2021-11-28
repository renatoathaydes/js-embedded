package bench;

import bench.state.BasicArithmeticsState;
import bench.state.D3ScaleState;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

@Fork(value = 2, warmups = 2)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class Benchmarks {

    @Benchmark
    public Object basicArithmetics(BasicArithmeticsState state) {
        return state.runScript();
    }

    @Benchmark
    public Object useD3ScaleLibrary(D3ScaleState state) {
        return state.runScript();
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}
