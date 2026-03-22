### Reflexion

A "fast" and "powerful" implementation library for Java reflection.

```kts
repositories {
    mavenCentral()
}
dependencies {
    implementation("io.github.lumine1909:reflexion:0.3.2")
}
```

Performance: almost same as `MethodHandle`/`VarHandle` with `static final` modifier, much faster than them without it.

| Benchmark                    | Mode | Cnt | Score  | Error  | Units |
|------------------------------|------|-----|--------|--------|-------|
| Benchmark.directAccess       | avgt | 20  | 0.543  | ±0.018 | ns/op |
| Benchmark.directMutate       | avgt | 20  | 0.800  | ±0.032 | ns/op |
| Benchmark.directCall         | avgt | 20  | 0.920  | ±0.032 | ns/op |
|                              |      |     |        |        |       |
| Benchmark.customAccess       | avgt | 20  | 1.238  | ±0.032 | ns/op |
| Benchmark.customAccessSF     | avgt | 20  | 0.500  | ±0.024 | ns/op |
| Benchmark.customAccessFast   | avgt | 20  | 0.873  | ±0.051 | ns/op |
| Benchmark.customAccessFastSF | avgt | 20  | 0.487  | ±0.025 | ns/op |
| Benchmark.customMutate       | avgt | 20  | 1.814  | ±0.156 | ns/op |
| Benchmark.customMutateSF     | avgt | 20  | 0.815  | ±0.041 | ns/op |
| Benchmark.customMutateFast   | avgt | 20  | 1.271  | ±0.061 | ns/op |
| Benchmark.customMutateFastSF | avgt | 20  | 0.808  | ±0.030 | ns/op |
| Benchmark.customCall         | avgt | 20  | 1.933  | ±0.180 | ns/op |
| Benchmark.customCallSF       | avgt | 20  | 0.828  | ±0.017 | ns/op |
|                              |      |     |        |        |       |
| Benchmark.vhAccess           | avgt | 20  | 6.525  | ±0.381 | ns/op |
| Benchmark.vhAccessSF         | avgt | 20  | 0.548  | ±0.015 | ns/op |
| Benchmark.vhMutate           | avgt | 20  | 12.536 | ±1.734 | ns/op |
| Benchmark.vhMutateSF         | avgt | 20  | 0.989  | ±0.245 | ns/op |
| Benchmark.mhCall             | avgt | 20  | 4.811  | ±0.156 | ns/op |
| Benchmark.mhCallSF           | avgt | 20  | 0.818  | ±0.016 | ns/op |
|                              |      |     |        |        |       |
| Benchmark.reflectAccess      | avgt | 20  | 5.272  | ±0.091 | ns/op |
| Benchmark.reflectAccessSF    | avgt | 20  | 0.515  | ±0.048 | ns/op |
| Benchmark.reflectMutate      | avgt | 20  | 6.887  | ±0.107 | ns/op |
| Benchmark.reflectMutateSF    | avgt | 20  | 1.093  | ±0.204 | ns/op |
| Benchmark.reflectCall        | avgt | 20  | 8.578  | ±0.228 | ns/op |
| Benchmark.reflectCallSF      | avgt | 20  | 1.017  | ±0.071 | ns/op |


`SF`: static final modifier.

------
<details>
  <summary>Benchmark code</summary>

```java
// Benchmark.java

package io.github.lumine1909;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

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
            .warmupTime(org.openjdk.jmh.runner.options.TimeValue.seconds(1))
            .measurementTime(org.openjdk.jmh.runner.options.TimeValue.seconds(1))
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
    public String reflectAccessSF() {
        return (String) final$vh$value.get(object);
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
        return (String) mh$call.invoke(object, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String mhCallSF() throws Throwable {
        return (String) final$mh$call.invoke(object, 42);
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
