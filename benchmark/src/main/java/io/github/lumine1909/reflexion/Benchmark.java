package io.github.lumine1909.reflexion;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static io.github.lumine1909.reflexion.internal.UnsafeUtil.IMPL_LOOKUP;

public class Benchmark {

    static class A {
        private String value = "42";

        public String call(int arg) {
            value = "42";
            return value;
        }
    }

    private static final A OBJECT = new A();

    private static Method reflect$call;
    private static final Method final$reflect$call;

    private static Field reflect$value;
    private static final Field final$reflect$value;

    private static MethodHandle mh$call;
    private static final MethodHandle final$mh$call;

    private static VarHandle vh$value;
    private static final VarHandle final$vh$value;

    private static io.github.lumine1909.reflexion.Field<String> custom$value =
        io.github.lumine1909.reflexion.Field.of(A.class, "value");
    private static final io.github.lumine1909.reflexion.Field<String> final$custom$value =
        io.github.lumine1909.reflexion.Field.of(A.class, "value");

    private static io.github.lumine1909.reflexion.Method<String> custom$call =
        io.github.lumine1909.reflexion.Method.of(A.class, "call", String.class, int.class);
    private static final io.github.lumine1909.reflexion.Method<String> final$custom$call =
        io.github.lumine1909.reflexion.Method.of(A.class, "call", String.class, int.class);

    static {
        try {
            reflect$call = A.class.getDeclaredMethod("call", int.class);
            reflect$call.setAccessible(true);
            final$reflect$call = reflect$call;

            reflect$value = A.class.getDeclaredField("value");
            reflect$value.setAccessible(true);
            final$reflect$value = reflect$value;

            mh$call = IMPL_LOOKUP.unreflect(reflect$call);
            final$mh$call = mh$call;

            vh$value = IMPL_LOOKUP.unreflectVarHandle(reflect$value);
            final$vh$value = vh$value;
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void main(String[] args) throws RunnerException {
        new java.io.File("build/reports/jmh").mkdirs();

        Options options = new OptionsBuilder()
            .include(Benchmark.class.getName())
            .forks(1)
            .warmupIterations(10)
            .measurementIterations(20)
            .warmupTime(TimeValue.seconds(1))
            .measurementTime(TimeValue.seconds(1))
            .shouldDoGC(false)
            .threads(1)
            .mode(Mode.AverageTime)
            .timeUnit(TimeUnit.NANOSECONDS)
            .resultFormat(ResultFormatType.JSON)
            .result("build/reports/jmh/results.json")
            .build();

        new Runner(options).run();
    }

    // ---------------------------------------------------------------------
    // Direct
    // ---------------------------------------------------------------------

    @org.openjdk.jmh.annotations.Benchmark
    public String directAccess() {
        return OBJECT.value;
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void directMutate() {
        OBJECT.value = "42";
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String directCall() {
        return OBJECT.call(42);
    }

    // ---------------------------------------------------------------------
    // Reflexion lib
    // ---------------------------------------------------------------------

    @org.openjdk.jmh.annotations.Benchmark
    public String customAccess() {
        return custom$value.get(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customAccessFast() {
        return custom$value.getFast(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customAccessSF() {
        return final$custom$value.get(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customAccessFastSF() {
        return final$custom$value.getFast(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutate() {
        custom$value.set(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutateFast() {
        custom$value.setFast(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutateSF() {
        final$custom$value.set(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutateFastSF() {
        final$custom$value.setFast(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customCall() {
        return custom$call.invoke(OBJECT, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customCallSF() {
        return final$custom$call.invoke(OBJECT, 42);
    }

    // ---------------------------------------------------------------------
    // VarHandle / MethodHandle
    // ---------------------------------------------------------------------

    @org.openjdk.jmh.annotations.Benchmark
    public String vhAccess() {
        return (String) vh$value.get(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String vhAccessSF() {
        return (String) final$vh$value.get(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void vhMutate() {
        vh$value.set(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void vhMutateSF() {
        final$vh$value.set(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String mhCall() throws Throwable {
        return (String) mh$call.invokeExact(OBJECT, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String mhCallSF() throws Throwable {
        return (String) final$mh$call.invokeExact(OBJECT, 42);
    }

    // ---------------------------------------------------------------------
    // Java reflection
    // ---------------------------------------------------------------------

    @org.openjdk.jmh.annotations.Benchmark
    public String reflectAccess() throws ReflectiveOperationException {
        return (String) reflect$value.get(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String reflectAccessSF() throws ReflectiveOperationException {
        return (String) final$reflect$value.get(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void reflectMutate() throws ReflectiveOperationException {
        reflect$value.set(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void reflectMutateSF() throws ReflectiveOperationException {
        final$reflect$value.set(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String reflectCall() throws ReflectiveOperationException {
        return (String) reflect$call.invoke(OBJECT, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String reflectCallSF() throws ReflectiveOperationException {
        return (String) final$reflect$call.invoke(OBJECT, 42);
    }
}