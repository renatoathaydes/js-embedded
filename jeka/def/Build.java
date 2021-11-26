import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.java.JkJavaProcess;
import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.java.project.JkJavaProject;
import dev.jeka.core.tool.JkClass;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

import java.util.jar.Attributes;
import java.util.stream.LongStream;

class Build extends JkClass {
    static final String PROJECT_VERSION = "1.0";

    final JkPluginJava java = getPlugin(JkPluginJava.class);

    @Override
    protected void setup() {
        configureManifest()
                .simpleFacade()
                .setJavaVersion(JkJavaVersion.V11)
                .setCompileDependencies(this::compileDependencies)
                .setTestDependencies(this::testDependencies)
                .setPublishedMavenModuleId("com.athaydes.js:js-embedded")
                .setPublishedMavenVersion(PROJECT_VERSION);
    }

    private JkDependencySet compileDependencies(JkDependencySet deps) {
        return deps;
    }

    private JkDependencySet testDependencies(JkDependencySet deps) {
        return deps.and("org.junit.jupiter:junit-jupiter:5.6.2");
    }

    private JkJavaProject configureManifest() {
        return java.getProject().getConstruction().getManifest()
                .addMainClass("com.athaydes.js.embedded.JsEmbed")
                .addMainAttribute(Attributes.Name.IMPLEMENTATION_VERSION, PROJECT_VERSION)
                .__.__;
    }

    public void cleanPack() {
        clean();
        java.pack();
    }

    @JkDoc("Compiles and run the main class.")
    public void run() {
        var jar = java.getProject().getPublication().getArtifactProducer().getMainArtifactPath();
        Util.time("Java process", 10, () -> {
            JkJavaProcess.of()
                    .withPrintCommand(false)
                    .andCommandLine("-Dnashorn.args=--no-deprecation-warning")
                    .runJarSync(jar);
        });
    }

    public static void main(String[] args) {
        JkInit.instanceOf(Build.class, args).cleanPack();
    }

}

class Util {
    static void time(String description, int times, Runnable action) {
        System.out.println("Running '" + description + "'... ");
        var start = System.currentTimeMillis();
        long[] runs = new long[times];
        for (int i = 0; i < times; i++) {
            action.run();
            var now = System.currentTimeMillis();
            runs[i] = now - start;
            start = now;
        }
        System.out.println("Result: " + LongStream.of(runs).summaryStatistics());
    }
}
