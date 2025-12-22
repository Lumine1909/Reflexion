### Reflexion

A "fast" implementation library for Java reflection.

| Benchmark               | Mode | Cnt | Score (ns/op) | Error (ns/op) |
|-------------------------|------|-----|---------------|---------------|
| Benchmark.directAccess  | avgt | 30  | 0.729         | ±0.022        |
| Benchmark.directMutate  | avgt | 30  | 1.233         | ±0.013        |
| Benchmark.directCall    | avgt | 30  | 1.254         | ±0.038        |
| Benchmark.customAccess  | avgt | 30  | 1.392         | ±0.026        |
| Benchmark.customMutate  | avgt | 30  | 1.650         | ±0.041        |
| Benchmark.customCall    | avgt | 30  | 6.326         | ±0.360        |
| Benchmark.mhAccess      | avgt | 30  | 4.973         | ±0.182        |
| Benchmark.mhMutate      | avgt | 30  | 4.914         | ±0.163        |
| Benchmark.mhCall        | avgt | 30  | 4.389         | ±0.265        |
| Benchmark.reflectAccess | avgt | 30  | 6.145         | ±0.107        |
| Benchmark.reflectMutate | avgt | 30  | 8.567         | ±0.655        |
| Benchmark.reflectCall   | avgt | 30  | 8.197         | ±0.381        |
