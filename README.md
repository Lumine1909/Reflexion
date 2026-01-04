### Reflexion

A "fast" and "powerful" implementation library for Java reflection.

| Benchmark           | Mode | Cnt | Score | Error  | Units |
|---------------------|------|-----|-------|--------|-------|
| directAccess        | avgt | 20  | 0.736 | ±0.045 | ns/op |
| directMutate        | avgt | 20  | 1.228 | ±0.073 | ns/op |
| directCall          | avgt | 20  | 1.461 | ±0.050 | ns/op |
|                     |      |     |       |        |       |
| customAccess        | avgt | 20  | 1.448 | ±0.080 | ns/op |
| customAccessFast    | avgt | 20  | 1.091 | ±0.038 | ns/op |
| customAccessSF      | avgt | 20  | 0.923 | ±0.028 | ns/op |
| customAccessFastSF  | avgt | 20  | 0.733 | ±0.023 | ns/op |
|                     |      |     |       |        |       |
| customMutate        | avgt | 20  | 1.942 | ±0.031 | ns/op |
| customMutateFast    | avgt | 20  | 1.948 | ±0.074 | ns/op |
| customMutateSF      | avgt | 20  | 1.478 | ±0.075 | ns/op |
| customMutateFastSF* | avgt | 20  | 1.706 | ±0.057 | ns/op |
|                     |      |     |       |        |       |
| customCall          | avgt | 20  | 6.087 | ±0.113 | ns/op |
| customCallFast      | avgt | 20  | 2.587 | ±0.087 | ns/op |
| customCallSF        | avgt | 20  | 5.514 | ±0.115 | ns/op |
| customCallFastSF    | avgt | 20  | 1.901 | ±0.050 | ns/op |
|                     |      |     |       |        |       |
| vhAccess            | avgt | 20  | 5.818 | ±0.284 | ns/op |
| vhAccessSF          | avgt | 20  | 0.743 | ±0.023 | ns/op |
| vhMutate            | avgt | 20  | 6.760 | ±0.464 | ns/op |
| vhMutateSF          | avgt | 20  | 1.117 | ±0.041 | ns/op |
| mhCall              | avgt | 20  | 5.087 | ±0.164 | ns/op |
| mhCallSF            | avgt | 20  | 1.535 | ±0.105 | ns/op |
|                     |      |     |       |        |       |
| reflectAccess       | avgt | 20  | 5.956 | ±0.273 | ns/op |
| reflectAccessSF     | avgt | 20  | 0.734 | ±0.017 | ns/op |
| reflectMutate       | avgt | 20  | 7.365 | ±0.468 | ns/op |
| reflectMutateSF     | avgt | 20  | 1.702 | ±0.144 | ns/op |
| reflectCall         | avgt | 20  | 7.681 | ±0.539 | ns/op |
| reflectCallSF       | avgt | 20  | 1.924 | ±0.042 | ns/op |

`SF`: static final modifier.

`*`: Due to not actually "fast", the implement is changed to the same as "customMutate".

------
<details>
  <summary>Benchmark code</summary>

```java
// Benchmark.java
package com.example;

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
            method$call = A.class.getDeclaredMethod("call", int.class);
            method$call.setAccessible(true);

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
    public String customCallFast() {
        return custom$call.invokeFast(object, 42);
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
    public void customMutateFast() {
        custom$value.setFast(object, "42");
    }


    @org.openjdk.jmh.annotations.Benchmark
    public String customCallSF() {
        return final$custom$call.invoke(object, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public String customCallFastSF() {
        return final$custom$call.invokeFast(object, 42);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutateSF() {
        final$custom$value.set(object, "42");
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void customMutateFastSF() {
        final$custom$value.setFast(object, "42");
    }
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
    implementation("io.github.lumine1909:reflexion:2.0.0")
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
