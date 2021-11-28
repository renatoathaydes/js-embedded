import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.java.JkJavaProcess;
import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.java.project.JkJavaProject;
import dev.jeka.core.tool.JkClass;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

import java.util.jar.Attributes;

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
        return deps.and(Dependencies.D3_SCALE2)
                .and(Dependencies.UNDERSCORE_JS1);
    }

    private JkDependencySet testDependencies(JkDependencySet deps) {
        return deps.and(Dependencies.JUNIT5)
                .and(Dependencies.JMH1)
                .and(Dependencies.JMH_ANN1);
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

    @JkDoc("Runs the project jar.")
    public void run() {
        var jar = java.getProject().getPublication().getArtifactProducer().getMainArtifactPath();
        JkJavaProcess.of()
                .withPrintCommand(false)
                .andCommandLine("-Dnashorn.args=--no-deprecation-warning")
                .runJarSync(jar);
    }

    @JkDoc("Runs the benchmarks.")
    public void bench() {
        java.getProject().getConstruction().getTesting().getCompilation().runIfNecessary();

        var testClasspath = java.getProject().getConstruction()
                .getTesting().getTestClasspath();

        JkJavaProcess.of()
                .withClasspath(testClasspath)
                .runClassSync("bench.Benchmarks");
    }

    public static void main(String[] args) {
        JkInit.instanceOf(Build.class, args).cleanPack();
    }

}
