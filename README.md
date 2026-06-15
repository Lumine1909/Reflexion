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

The main performance trick is creating a stable concrete call site around MethodHandle/VarHandle, so the JIT can inline through the wrapper and recover the underlying target.

Benchmark:

| Benchmark          | Mode | Cnt | Score  | Error  | Units |
|--------------------|------|-----|--------|--------|-------|
| directAccess       | avgt | 20  | 0.472  | ±0.002 | ns/op |
| directMutate       | avgt | 20  | 0.744  | ±0.021 | ns/op |
| directCall         | avgt | 20  | 0.802  | ±0.010 | ns/op |
|                    |      |     |        |        |       |
| customAccess       | avgt | 20  | 1.341  | ±0.098 | ns/op |
| customAccessFast   | avgt | 20  | 0.825  | ±0.044 | ns/op |
| customAccessSF     | avgt | 20  | 0.464  | ±0.004 | ns/op |
| customAccessFastSF | avgt | 20  | 0.471  | ±0.022 | ns/op |
| customMutate       | avgt | 20  | 1.230  | ±0.007 | ns/op |
| customMutateFast   | avgt | 20  | 1.186  | ±0.007 | ns/op |
| customMutateSF     | avgt | 20  | 0.723  | ±0.004 | ns/op |
| customMutateFastSF | avgt | 20  | 0.719  | ±0.007 | ns/op |
| customCall         | avgt | 20  | 1.317  | ±0.008 | ns/op |
| customCallSF       | avgt | 20  | 0.793  | ±0.008 | ns/op |
|                    |      |     |        |        |       |
| vhAccess           | avgt | 20  | 6.696  | ±0.122 | ns/op |
| vhAccessSF         | avgt | 20  | 0.560  | ±0.007 | ns/op |
| vhMutate           | avgt | 20  | 10.021 | ±0.294 | ns/op |
| vhMutateSF         | avgt | 20  | 0.787  | ±0.011 | ns/op |
| mhCall             | avgt | 20  | 4.510  | ±0.081 | ns/op |
| mhCallSF           | avgt | 20  | 0.806  | ±0.005 | ns/op |
|                    |      |     |        |        |       |
| reflectAccess      | avgt | 20  | 6.232  | ±0.084 | ns/op |
| reflectAccessSF    | avgt | 20  | 0.725  | ±0.014 | ns/op |
| reflectMutate      | avgt | 20  | 6.875  | ±0.627 | ns/op |
| reflectMutateSF    | avgt | 20  | 1.017  | ±0.103 | ns/op |
| reflectCall        | avgt | 20  | 10.563 | ±0.820 | ns/op |
| reflectCallSF      | avgt | 20  | 1.152  | ±0.014 | ns/op |

`SF`: static final modifier.

Environment:
```
java version "25.0.3" 2026-04-21 LTS
Java(TM) SE Runtime Environment Oracle GraalVM 25.0.3+9.1 (build 25.0.3+9-LTS-jvmci-b01)
Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM 25.0.3+9.1 (build 25.0.3+9-LTS-jvmci-b01, mixed mode, sharing)
```

You can clone this repo and run the benchmark yourself, or check the action page for ci benchmark.