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
| directAccess       | avgt | 20  | 0.232 | ±0.010 | ns/op |
| directMutate       | avgt | 20  | 0.457 | ±0.003 | ns/op |
| directCall         | avgt | 20  | 0.368 | ±0.007 | ns/op |
|                    |      |     |       |        |       |
| customAccess       | avgt | 20  | 0.574 | ±0.003 | ns/op |
| customAccessFast   | avgt | 20  | 0.431 | ±0.003 | ns/op |
| customAccessSF     | avgt | 20  | 0.227 | ±0.002 | ns/op |
| customAccessFastSF | avgt | 20  | 0.228 | ±0.002 | ns/op |
| customMutate       | avgt | 20  | 0.684 | ±0.003 | ns/op |
| customMutateFast   | avgt | 20  | 0.561 | ±0.004 | ns/op |
| customMutateSF     | avgt | 20  | 0.459 | ±0.006 | ns/op |
| customMutateFastSF | avgt | 20  | 0.450 | ±0.009 | ns/op |
| customCall         | avgt | 20  | 0.574 | ±0.003 | ns/op |
| customCallSF       | avgt | 20  | 0.360 | ±0.008 | ns/op |
|                    |      |     |       |        |       |
| vhAccess           | avgt | 20  | 3.040 | ±0.036 | ns/op |
| vhAccessSF         | avgt | 20  | 0.228 | ±0.004 | ns/op |
| vhMutate           | avgt | 20  | 3.385 | ±0.079 | ns/op |
| vhMutateSF         | avgt | 20  | 0.450 | ±0.007 | ns/op |
| mhCall             | avgt | 20  | 2.159 | ±0.009 | ns/op |
| mhCallSF           | avgt | 20  | 0.360 | ±0.003 | ns/op |
|                    |      |     |       |        |       |
| reflectAccess      | avgt | 20  | 2.755 | ±0.013 | ns/op |
| reflectAccessSF    | avgt | 20  | 0.425 | ±0.005 | ns/op |
| reflectMutate      | avgt | 20  | 3.296 | ±0.013 | ns/op |
| reflectMutateSF    | avgt | 20  | 0.455 | ±0.004 | ns/op |
| reflectCall        | avgt | 20  | 3.655 | ±0.019 | ns/op |
| reflectCallSF      | avgt | 20  | 0.539 | ±0.008 | ns/op |

`SF`: static final modifier.

Environment:
```
java 25.0.5.1 2026-11-17 LTS
Java(TM) SE Runtime Environment Oracle GraalVM 25.0.5.1+4.1 (build 25.0.5.1+4-LTS-jvmci-b01)
Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM 25.0.5.1+4.1 (build 25.0.5.1+4-LTS-jvmci-b01, mixed mode, sharing)
```

You can clone this repo and run the benchmark yourself, or check the action page for ci benchmark.
