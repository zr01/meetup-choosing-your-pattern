# Specs of the machine

Apple M3 Pro (5 P-cores, 6 E-cores) [I have no idea how this works, I promise to read up on it later]

# Experiment

Calling POST /person/sync inserts a record to the database
Using k6 for load testing -- 120s

## Application Setup

Spring Boot 3.2
Postgres 16
Spring Data JPA
-Xmx2g
Provided 2 ways to save to DB: synchronous and async (with a fixed pool of 500 threads)
Reactive API is also created as a separate project utilising r2dbc

```
Legends
* - best metric
^ - worst metric
```

## 10 Virtual Users Test

| HTTP Thread     | Save to DB  | Throughput | Total Requests | CPU Usage | p95 Response Time | p50 Response Time | DB Persist Lag | Additional time to complete |
|-----------------|-------------|------------|----------------|-----------|-------------------|-------------------|----------------|-----------------------------|
| Non-blocking IO | synchronous | 5529 rps   | 663367         | 11% *     | 2.85ms            | 1.64ms            | NA             | NA                          |
| Virtual Threads | synchronous | 5298 rps ^ | 635720         | 15%       | 2.84ms            | 1.68ms ^          | NA             | NA                          |
| Non-blocking IO | async       | 8383 rps * | 1006315        | 18% ^     | 4.08ms            | 584.54µs *        | 200k-250k ^    | 1m                          |
| Virtual Threads | async       | 6850 rps   | 822087         | 17%       | 5.22ms ^          | 688.91µs          | 20k *          | 15s                         |
| Reactive        | reactive    | 7345 rps   | 881529         | 17%       | 1.96ms *          | 1.23ms            | NA             | NA                          |

## 50 Virtual Users Test

| HTTP Thread     | Save to DB  | Throughput  | Total Requests | CPU Usage | p95 Response Time | p50 Response Time | DB Persist Lag | Additional time to complete |
|-----------------|-------------|-------------|----------------|-----------|-------------------|-------------------|----------------|-----------------------------|
| Non-blocking IO | synchronous | 7417 rps    | 890088         | 17%       | 11.7ms            | 6.29ms            | NA             | NA                          |
| Virtual Threads | synchronous | 5688 rps ^  | 822087         | 16% *     | 15.29ms           | 7.81ms ^          | NA             | NA                          |
| Non-blocking IO | async       | 10174 rps * | 1221007        | 20%       | 23.42ms           | 1.36ms *          | 109k ^         | 1m                          |
| Virtual Threads | async       | 8225 rps    | 987157         | 21% ^     | 32.82ms ^         | 1.82ms            | 50k *          | 15s                         |
| Reactive        | reactive    | 8194 rps    | 983343         | 21% ^     | 7.63ms *          | 5.91ms            | NA             | NA                          |

## 100 Virtual Users Test

| HTTP Thread     | Save to DB  | Throughput  | Total Requests | CPU Usage | p95 Response Time | p50 Response Time | DB Persist Lag | Additional time to complete |
|-----------------|-------------|-------------|----------------|-----------|-------------------|-------------------|----------------|-----------------------------|
| Non-blocking IO | synchronous | 7560 rps    | 907369         | 15% *     | 28.65ms           | 12.31ms           | NA             | NA                          |
| Virtual Threads | synchronous | 5458 rps ^  | 655072         | 16%       | 34.43ms           | 16.69ms ^         | NA             | NA                          |
| Non-blocking IO | async       | 10223 rps * | 1226975        | 21% ^     | 51.16ms           | 1.19ms *          | 450-480k ^     | 1m-2m                       |
| Virtual Threads | async       | 7274 rps    | 872774         | 18%       | 68.47ms ^         | 2.35ms            | 50k *          | 15-30s                      |
| Reactive        | reactive    | 7923 rps    | 950922         | 20%       | 16.23ms *         | 12.03ms           | NA             | NA                          |

# Observations

- Virtual Threads does not provide additional benefit in the throughput, if anything it is slower than Non-blocking IO Threads as shown in our initial
  tests with direct calls to the DB once an HTTP POST is processed
- However, it shows that using virtual threads for http connections increases the throughput of asynchronous tasks due to the dedicated pool not
  directly being impacted by virtual thread workloads, this is shown through the lag from the async save to the database
- Reactive provided the most favorable results considering a balancing act in this comparison as it showed a near instantaneous persistence of the
  entity into the DB without sacrificing
  throughput. Balance in this case is where we **do not compromise on the data integrity** and **retain a favorable throughput**.

# Conclusion

Using virtual threads in the http connections provides benefits to asynchronous workloads bound to the same process. This is quite effective in an
application that has both a producing and consuming pattern.

In the experiment where the configuration is for virtual threads to handle HTTP connections and a pool dedicated to saving to the database was
employed. The lag between the data being saved and the calls being made to the service is way less and this is where virtual threads really gives the
application a boost. However, it did produce a lower throughput compared to what Non-IO blocking threads can provide.

Even with the advent of virtual threads, reactive runtime produces the best results. However, this should only be taken into consideration when
scalability and throughput really becomes an issue as reactive programming still has pitfalls with readability. Alternatives is of course to use a
different language like Kotlin that may provide a bit of syntactical sugar to use reactive patterns. At the end of the day, your use case should
define which pattern you should use, deciding to use a pattern that is proven in benchmarks to work well is great but it limits the use or
proliferation of said pattern due to possible lack of skillsets to maintain.

# Real World Example

I did however, despite seeing these tests turned on `virtual threads` in a production application.

A non-critical workload -- it produced a favorable outcome where the virtual threads provided the needed support for the service to become performant.

The configuration of the application was to only service API requests and send the workload to an event broker. In comparison to the use cases
provided earlier, the service also runs a "consumer" for the completable future that probably shares the resource with virtual threads running and a
fixed pool of threads.