plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openjdk.jmh:jmh-core:1.37")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    implementation(project(":"))
}

tasks {
    withType<JavaCompile>().configureEach {
        options.compilerArgs.addAll(
            listOf("--enable-preview", "--release", "25")
        )
    }
    withType<Test>().configureEach {
        jvmArgs("--enable-preview")
    }
    withType<JavaExec>().configureEach {
        jvmArgs("--enable-preview")
    }
    register<JavaExec>("benchmark") {
        group = "benchmark"
        description = "Run JMH benchmarks"
        classpath = sourceSets.main.get().runtimeClasspath
        mainClass.set("io.github.lumine1909.reflexion.Benchmark")
        args(
            "-rf", "json",
            "-rff", layout.buildDirectory.file("reports/jmh/results.json").get().asFile.absolutePath
        )
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}