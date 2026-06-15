### Reflexion

A "fast" and "powerful" lightweight Java reflection library.

```kotlin
repositories {
    mavenCentral()
}
dependencies {
    implementation("io.github.lumine1909:reflexion:0.5.1")
}
```

Performance: almost the same as `MethodHandle`/`VarHandle` when stored in stable fields, especially `static final`. Compared with non-final raw `MethodHandle`/`VarHandle` fields, Reflexion can be significantly faster.
Benchmark:

The main performance trick is creating a stable concrete call site around MethodHandle/VarHandle, so the JIT can inline through the wrapper and recover the underlying target.

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

Environment:
```
java version "25.0.2" 2026-01-20 LTS
Java(TM) SE Runtime Environment Oracle GraalVM 25.0.2+10.1 (build 25.0.2+10-LTS-jvmci-b01)
Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM 25.0.2+10.1 (build 25.0.2+10-LTS-jvmci-b01, mixed mode, sharing)
```

You can clone this repo and run the benchmark yourself, or check the action page for ci benchmark.