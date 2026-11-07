### Reflexion

A "fast" and "powerful" lightweight Java reflection library.

```kotlin
repositories {
    mavenCentral()
}
dependencies {
    implementation("io.github.lumine1909:reflexion:0.6.0")
}
```

Performance: almost the same as `MethodHandle`/`VarHandle` when stored in stable fields, especially `static final`. Compared with non-final raw `MethodHandle`/`VarHandle` fields, Reflexion can be significantly faster.

The main performance trick is creating a stable concrete call site around MethodHandle/VarHandle, so the JIT can inline through the wrapper and recover the underlying target.

You can check out the [related articles](https://lumine1909.github.io/categories/reflexion/) for more information.

Benchmark:

| Benchmark          | Mode | Cnt | Score | Error  | Units |
|--------------------|------|-----|-------|--------|-------|
| directAccess       | avgt | 20  | 0.225 | ±0.002 | ns/op |
| directMutate       | avgt | 20  | 0.474 | ±0.007 | ns/op |
| directCall         | avgt | 20  | 0.375 | ±0.018 | ns/op |
|                    |      |     |       |        |       |
| customAccess       | avgt | 20  | 0.714 | ±0.006 | ns/op |
| customAccessFast   | avgt | 20  | 0.526 | ±0.001 | ns/op |
| customAccessSF     | avgt | 20  | 0.225 | ±0.002 | ns/op |
| customAccessFastSF | avgt | 20  | 0.225 | ±0.002 | ns/op |
| customMutate       | avgt | 20  | 0.707 | ±0.003 | ns/op |
| customMutateFast   | avgt | 20  | 0.705 | ±0.005 | ns/op |
| customMutateSF     | avgt | 20  | 0.468 | ±0.004 | ns/op |
| customMutateFastSF | avgt | 20  | 0.455 | ±0.004 | ns/op |
| customCall         | avgt | 20  | 0.569 | ±0.004 | ns/op |
| customCallSF       | avgt | 20  | 0.355 | ±0.003 | ns/op |
|                    |      |     |       |        |       |
| vhAccess           | avgt | 20  | 3.018 | ±0.015 | ns/op |
| vhAccessSF         | avgt | 20  | 0.230 | ±0.002 | ns/op |
| vhMutate           | avgt | 20  | 3.790 | ±0.010 | ns/op |
| vhMutateSF         | avgt | 20  | 0.468 | ±0.005 | ns/op |
| mhCall             | avgt | 20  | 2.156 | ±0.007 | ns/op |
| mhCallSF           | avgt | 20  | 0.369 | ±0.001 | ns/op |
|                    |      |     |       |        |       |
| reflectAccess      | avgt | 20  | 2.780 | ±0.031 | ns/op |
| reflectAccessSF    | avgt | 20  | 0.422 | ±0.001 | ns/op |
| reflectMutate      | avgt | 20  | 3.289 | ±0.008 | ns/op |
| reflectMutateSF    | avgt | 20  | 0.451 | ±0.002 | ns/op |
| reflectCall        | avgt | 20  | 3.615 | ±0.025 | ns/op |
| reflectCallSF      | avgt | 20  | 0.604 | ±0.001 | ns/op |
`SF`: static final modifier.

Environment:
```
java version "25.0.4" 2026-07-21 LTS
Java(TM) SE Runtime Environment Oracle GraalVM 25.0.4+7.1 (build 25.0.4+7-LTS-jvmci-b01)
Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM 25.0.4+7.1 (build 25.0.4+7-LTS-jvmci-b01, mixed mode, sharing)
```

You can clone this repo and run the benchmark yourself, or check the action page for ci benchmark.
