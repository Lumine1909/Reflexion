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

    private static Method reflectCall;
    private static final Method REFLECT_CALL;

    private static Field reflectValue;
    private static final Field REFLECT_VALUE;

    private static MethodHandle mhCall;
    private static final MethodHandle MH_CALL;

    private static VarHandle vhValue;
    private static final VarHandle VH_VALUE;

    private static io.github.lumine1909.reflexion.Field<String> customValue =
        io.github.lumine1909.reflexion.Field.of(A.class, "value");
    private static final io.github.lumine1909.reflexion.Field<String> CUSTOM_VALUE =
        io.github.lumine1909.reflexion.Field.of(A.class, "value");

    private static io.github.lumine1909.reflexion.Method<String> customCall =
        io.github.lumine1909.reflexion.Method.of(A.class, "call", String.class, int.class);
    private static final io.github.lumine1909.reflexion.Method<String> CUSTOM_CALL =
        io.github.lumine1909.reflexion.Method.of(A.class, "call", String.class, int.class);

    static {
        try {
            reflectCall = A.class.getDeclaredMethod("call", int.class);
            reflectCall.setAccessible(true);
            REFLECT_CALL = reflectCall;

            reflectValue = A.class.getDeclaredField("value");
            reflectValue.setAccessible(true);
            REFLECT_VALUE = reflectValue;

            mhCall = IMPL_LOOKUP.unreflect(reflectCall);
            MH_CALL = mhCall;

            vhValue = IMPL_LOOKUP.unreflectVarHandle(reflectValue);
            VH_VALUE = vhValue;
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void main(String[] args) throws RunnerException {
        new java.io.File("build/reports/jmh").mkdirs();

        Options options = new OptionsBuilder()
            .include(Benchmark.class.getName())
            .forks(1)
            .warmupIterations(0)
            .measurementIterations(1)
            .warmupTime(TimeValue.seconds(1))
            .measurementTime(TimeValue.seconds(1))
            .shouldDoGC(false)
            .threads(1)
            .mode(Mode.AverageTime)
            .timeUnit(TimeUnit.NANOSECONDS)
            .resultFormat(org.openjdk.jmh.results.format.ResultFormatType.JSON)
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
        return customValue.get(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customAccessFast() {
        return customValue.getFast(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customAccessSF() {
        return CUSTOM_VALUE.get(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customAccessFastSF() {
        return CUSTOM_VALUE.getFast(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutate() {
        customValue.set(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutateFast() {
        customValue.setFast(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutateSF() {
        CUSTOM_VALUE.set(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutateFastSF() {
        CUSTOM_VALUE.setFast(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customCall() {
        return customCall.invoke(OBJECT, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customCallSF() {
        return CUSTOM_CALL.invoke(OBJECT, 42);
    }

    // ---------------------------------------------------------------------
    // VarHandle / MethodHandle
    // ---------------------------------------------------------------------

    @org.openjdk.jmh.annotations.Benchmark
    public String vhAccess() {
        return (String) vhValue.get(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String vhAccessSF() {
        return (String) VH_VALUE.get(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void vhMutate() {
        vhValue.set(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void vhMutateSF() {
        VH_VALUE.set(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String mhCall() throws Throwable {
        return (String) mhCall.invokeExact(OBJECT, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String mhCallSF() throws Throwable {
        return (String) MH_CALL.invokeExact(OBJECT, 42);
    }

    // ---------------------------------------------------------------------
    // Java reflection
    // ---------------------------------------------------------------------

    @org.openjdk.jmh.annotations.Benchmark
    public String reflectAccess() throws ReflectiveOperationException {
        return (String) reflectValue.get(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String reflectAccessSF() throws ReflectiveOperationException {
        return (String) REFLECT_VALUE.get(OBJECT);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void reflectMutate() throws ReflectiveOperationException {
        reflectValue.set(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void reflectMutateSF() throws ReflectiveOperationException {
        REFLECT_VALUE.set(OBJECT, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String reflectCall() throws ReflectiveOperationException {
        return (String) reflectCall.invoke(OBJECT, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String reflectCallSF() throws ReflectiveOperationException {
        return (String) REFLECT_CALL.invoke(OBJECT, 42);
    }
}