### Reflexion

A "fast" and "powerful" implementation library for Java reflection.

Performance: almost same as `MethodHandle`/`VarHandle` with `static final` modifier, much faster than them without it.

| Benchmark                    | Mode | Cnt | Score | Error   | Units |
|------------------------------|------|-----|-------|---------|-------|
| Benchmark.directAccess       | avgt | 20  | 0.579 | ± 0.005 | ns/op |
| Benchmark.directCall         | avgt | 20  | 1.383 | ± 0.013 | ns/op |
| Benchmark.directMutate       | avgt | 20  | 0.933 | ± 0.006 | ns/op |
|                              |      |     |       |         |       |
| Benchmark.customAccess       | avgt | 20  | 1.384 | ± 0.021 | ns/op |
| Benchmark.customAccessFast   | avgt | 20  | 0.927 | ± 0.007 | ns/op |
| Benchmark.customAccessSF     | avgt | 20  | 0.576 | ± 0.007 | ns/op |
| Benchmark.customAccessFastSF | avgt | 20  | 0.572 | ± 0.010 | ns/op |
| Benchmark.customMutate       | avgt | 20  | 1.660 | ± 0.012 | ns/op |
| Benchmark.customMutateSF     | avgt | 20  | 0.969 | ± 0.062 | ns/op |
| Benchmark.customCall         | avgt | 20  | 2.505 | ± 0.016 | ns/op |
| Benchmark.customCallSF       | avgt | 20  | 1.380 | ± 0.010 | ns/op |
|                              |      |     |       |         |       |
| Benchmark.reflectAccess      | avgt | 20  | 5.590 | ± 0.046 | ns/op |
| Benchmark.reflectAccessFast  | avgt | 20  | 0.575 | ± 0.005 | ns/op |
| Benchmark.reflectMutate      | avgt | 20  | 6.935 | ± 0.057 | ns/op |
| Benchmark.reflectMutateFast  | avgt | 20  | 1.639 | ± 0.030 | ns/op |
| Benchmark.reflectCall        | avgt | 20  | 7.186 | ± 0.138 | ns/op |
| Benchmark.reflectCallFast    | avgt | 20  | 1.620 | ± 0.016 | ns/op |
|                              |      |     |       |         |       |
| Benchmark.vhAccess           | avgt | 20  | 5.238 | ± 0.046 | ns/op |
| Benchmark.vhAccessFast       | avgt | 20  | 0.577 | ± 0.007 | ns/op |
| Benchmark.vhMutate           | avgt | 20  | 6.188 | ± 0.069 | ns/op |
| Benchmark.vhMutateFast       | avgt | 20  | 1.044 | ± 0.006 | ns/op |
| Benchmark.mhCall             | avgt | 20  | 4.515 | ± 0.054 | ns/op |
| Benchmark.mhCallFast         | avgt | 20  | 1.381 | ± 0.012 | ns/op |


`SF`: static final modifier.

------
<details>
  <summary>Benchmark code</summary>

```java
// Benchmark.javapackage com.example;

import org.openjdk.jmh.annotations.*;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static io.github.lumine1909.reflexion.internal.UnsafeUtil.IMPL_LOOKUP;

@State(Scope.Thread)
public class Benchmark {

    static class A {

        public String call(int arg) {
            value = "42";
            return value;
        }

        private String value = "42";
    }

    private A object = new A();

    private static final Method final$method$call;
    private static Method method$call;

    private static final MethodHandle final$mh$call;
    private static MethodHandle mh$call;

    private static final Field final$field$value;
    private static Field field$value;

    private static final VarHandle final$vh$value;
    private static VarHandle vh$value;

    static {
        try {
            final Method m = A.class.getDeclaredMethod("call", int.class);
            m.setAccessible(true);

            method$call = m;

            final$method$call = method$call;
            mh$call = IMPL_LOOKUP.unreflect(method$call);
            final$mh$call = mh$call;

            field$value = A.class.getDeclaredField("value");
            field$value.setAccessible(true);
            final$field$value = field$value;

            vh$value = IMPL_LOOKUP.unreflectVarHandle(field$value);
            final$vh$value = vh$value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final io.github.lumine1909.reflexion.Field<String> final$custom$value = io.github.lumine1909.reflexion.Field.of(A.class, "value");
    private static io.github.lumine1909.reflexion.Field<String> custom$value = io.github.lumine1909.reflexion.Field.of(A.class, "value");

    private static final io.github.lumine1909.reflexion.Method<String> final$custom$call = io.github.lumine1909.reflexion.Method.of(A.class, "call", String.class, int.class);
    private static io.github.lumine1909.reflexion.Method<String> custom$call = io.github.lumine1909.reflexion.Method.of(A.class, "call", String.class, int.class);

    @org.openjdk.jmh.annotations.Benchmark
    public String directAccess() {
        return object.value;
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void directMutate() {
        object.value = "42";
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String directCall() {
        return object.call(42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String reflectAccess() throws Throwable {
        return (String) field$value.get(object);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String reflectAccessFast() {
        return (String) final$vh$value.get(object);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void reflectMutate() throws Throwable {
        field$value.set(object, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void reflectMutateFast() throws Throwable {
        final$field$value.set(object, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String reflectCall() throws Throwable {
        return (String) method$call.invoke(object, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String reflectCallFast() throws Throwable {
        return (String) final$method$call.invoke(object, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String mhCall() throws Throwable {
        return (String) mh$call.invoke(object, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String mhCallFast() throws Throwable {
        return (String) final$mh$call.invoke(object, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String vhAccess() {
        return (String) vh$value.get(object);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String vhAccessFast() {
        return (String) final$vh$value.get(object);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void vhMutate() {
        vh$value.set(object, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void vhMutateFast() {
        final$vh$value.set(object, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customCall() {
        return custom$call.invoke(object, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customCallSF() {
        return final$custom$call.invoke(object, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customAccess() {
        return custom$value.get(object);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customAccessFast() {
        return custom$value.getFast(object);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customAccessSF() {
        return final$custom$value.get(object);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customAccessFastSF() {
        return final$custom$value.getFast(object);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutate() {
        custom$value.set(object, "42");
    }

//    @org.openjdk.jmh.annotations.Benchmark
//    public void customMutateFast() {
//        custom$value.setFast(object, "42");
//    }

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutateSF() {
        final$custom$value.set(object, "42");
    }

//    @org.openjdk.jmh.annotations.Benchmark
//    public void customMutateFastSF() {
//        final$custom$value.setFast(object, "42");
//    }
}
```
```kotlin
// build.gradle.kts
plugins {
    java
    id("me.champeau.jmh") version "0.7.3"
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    jmh("org.openjdk.jmh:jmh-core:1.37")
    //jmh("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    implementation("io.github.lumine1909:reflexion:3.1.0")
    implementation("io.netty:netty-all:4.2.6.Final")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.named<Jar>("jmhJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
jmh {
    iterations = 20
    warmupIterations = 10
    fork = 1

    batchSize = 1
    warmupBatchSize = 1

    timeOnIteration = "1s"
    warmup = "1s"

    forceGC = false
    threads = 1

    benchmarkMode = listOf("avgt")
    timeUnit = "ns"
}
```
</details>