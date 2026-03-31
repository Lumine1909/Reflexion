### Reflexion

A "fast" and "powerful" implementation library for Java reflection.

```kotlin
repositories {
    mavenCentral()
}
dependencies {
    implementation("io.github.lumine1909:reflexion:0.3.2")
}
```

Performance: almost same as `MethodHandle`/`VarHandle` with `static final` modifier, much faster than them without it.

Benchmark Environment:
```
java version "25.0.2" 2026-01-20 LTS
Java(TM) SE Runtime Environment Oracle GraalVM 25.0.2+10.1 (build 25.0.2+10-LTS-jvmci-b01)
Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM 25.0.2+10.1 (build 25.0.2+10-LTS-jvmci-b01, mixed mode, sharing)
```

| Benchmark                    | Mode | Cnt | Score | Error  | Units |
|------------------------------|------|-----|-------|--------|-------|
| Benchmark.directAccess       | avgt | 20  | 0.481 | ±0.007 | ns/op |
| Benchmark.directMutate       | avgt | 20  | 0.732 | ±0.010 | ns/op |
| Benchmark.directCall         | avgt | 20  | 0.803 | ±0.007 | ns/op |
|                              |      |     |       |        |       |
| Benchmark.customAccess       | avgt | 20  | 1.169 | ±0.009 | ns/op |
| Benchmark.customAccessFast   | avgt | 20  | 0.827 | ±0.015 | ns/op |
| Benchmark.customAccessSF     | avgt | 20  | 0.474 | ±0.005 | ns/op |
| Benchmark.customAccessFastSF | avgt | 20  | 0.472 | ±0.004 | ns/op |
| Benchmark.customMutate       | avgt | 20  | 1.684 | ±0.014 | ns/op |
| Benchmark.customMutateFast   | avgt | 20  | 1.231 | ±0.042 | ns/op |
| Benchmark.customMutateSF     | avgt | 20  | 0.728 | ±0.009 | ns/op |
| Benchmark.customMutateFastSF | avgt | 20  | 0.728 | ±0.010 | ns/op |
| Benchmark.customCall         | avgt | 20  | 1.812 | ±0.020 | ns/op |
| Benchmark.customCallSF       | avgt | 20  | 0.803 | ±0.007 | ns/op |
|                              |      |     |       |        |       |
| Benchmark.vhAccess           | avgt | 20  | 5.970 | ±0.327 | ns/op |
| Benchmark.vhAccessSF         | avgt | 20  | 0.476 | ±0.007 | ns/op |
| Benchmark.vhMutate           | avgt | 20  | 7.739 | ±0.118 | ns/op |
| Benchmark.vhMutateSF         | avgt | 20  | 0.735 | ±0.011 | ns/op |
| Benchmark.mhCall             | avgt | 20  | 4.553 | ±0.063 | ns/op |
| Benchmark.mhCallSF           | avgt | 20  | 0.856 | ±0.022 | ns/op |
|                              |      |     |       |        |       |
| Benchmark.reflectAccess      | avgt | 20  | 5.191 | ±0.071 | ns/op |
| Benchmark.reflectAccessSF    | avgt | 20  | 0.601 | ±0.013 | ns/op |
| Benchmark.reflectMutate      | avgt | 20  | 7.480 | ±0.157 | ns/op |
| Benchmark.reflectMutateSF    | avgt | 20  | 0.967 | ±0.028 | ns/op |
| Benchmark.reflectCall        | avgt | 20  | 7.683 | ±0.062 | ns/op |
| Benchmark.reflectCallSF      | avgt | 20  | 1.009 | ±0.028 | ns/op |

`SF`: static final modifier.

------
<details>
  <summary>Benchmark code</summary>

```java
package io.github.lumine1909;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static io.github.lumine1909.reflexion.internal.UnsafeUtil.IMPL_LOOKUP;

public class Benchmark {

    static class A {

        public String call(int arg) {
            value = "42";
            return value;
        }

        private String value = "42";
    }

    private static final A object = new A();

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

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(Benchmark.class.getName())
            .forks(1)
            .warmupIterations(10)
            .measurementIterations(20)
            .warmupTime(TimeValue.seconds(1))
            .measurementTime(TimeValue.seconds(1))
            .warmupBatchSize(1)
            .measurementBatchSize(1)
            .shouldDoGC(false)
            .threads(1)
            .mode(Mode.AverageTime)
            .timeUnit(TimeUnit.NANOSECONDS)
            .build();

        new Runner(opt).run();
    }

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
    public String reflectAccessSF() throws Throwable {
        return (String) final$field$value.get(object);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void reflectMutate() throws Throwable {
        field$value.set(object, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void reflectMutateSF() throws Throwable {
        final$field$value.set(object, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String reflectCall() throws Throwable {
        return (String) method$call.invoke(object, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String reflectCallSF() throws Throwable {
        return (String) final$method$call.invoke(object, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String mhCall() throws Throwable {
        return (String) mh$call.invokeExact(object, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String mhCallSF() throws Throwable {
        return (String) final$mh$call.invokeExact(object, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String vhAccess() {
        return (String) vh$value.get(object);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String vhAccessSF() {
        return (String) final$vh$value.get(object);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void vhMutate() {
        vh$value.set(object, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void vhMutateSF() {
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

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutateSF() {
        final$custom$value.set(object, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutateFast() {
        custom$value.setFast(object, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutateFastSF() {
        final$custom$value.setFast(object, "42");
    }
}
```
</details>
