### Reflexion

A fast implementation library for Java reflection.

<details>
<summary>Benchmark Results</summary>

```csv
"Benchmark","Mode","Threads","Samples","Score","Score Error (99.9%)","Unit"
"com.example.Benchmark.customAccess","avgt",1,10,2251.208115,110.903348,"ns/op"
"com.example.Benchmark.customCall","avgt",1,10,5926.506612,98.515748,"ns/op"
"com.example.Benchmark.customMutate","avgt",1,10,1906.914484,40.849426,"ns/op"
"com.example.Benchmark.directAccess","avgt",1,10,1714.156725,25.140701,"ns/op"
"com.example.Benchmark.directCall","avgt",1,10,1450.620829,82.614426,"ns/op"
"com.example.Benchmark.directMutate","avgt",1,10,1441.838482,27.120427,"ns/op"
"com.example.Benchmark.reflectAccess","avgt",1,10,6191.017667,152.340028,"ns/op"
"com.example.Benchmark.reflectCall","avgt",1,10,7838.214473,222.530643,"ns/op"
"com.example.Benchmark.reflectMutate","avgt",1,10,8239.178111,416.702226,"ns/op"
```


| Benchmark         | Type   | Score (ns/op) | Direct Baseline (ns/op) | Slowdown × | Δ Time (ns) |
| :---------------- | :----- | ------------: | ----------------------: | ---------: | ----------: |
| **directAccess**  | access |       1714.16 |                 1714.16 |  **1.00×** |        0.00 |
| **directCall**    | call   |       1450.62 |                 1450.62 |  **1.00×** |        0.00 |
| **directMutate**  | mutate |       1441.84 |                 1441.84 |  **1.00×** |        0.00 |
| **customAccess**  | access |       2251.21 |                 1714.16 |  **1.31×** |     +537.05 |
| **customCall**    | call   |       5926.51 |                 1450.62 |  **4.08×** |    +4475.89 |
| **customMutate**  | mutate |       1906.91 |                 1441.84 |  **1.32×** |     +465.08 |
| **reflectAccess** | access |       6191.02 |                 1714.16 |  **3.61×** |    +4476.86 |
| **reflectCall**   | call   |       7838.21 |                 1450.62 |  **5.40×** |    +6387.59 |
| **reflectMutate** | mutate |       8239.18 |                 1441.84 |  **5.71×** |    +6797.34 |

</details>