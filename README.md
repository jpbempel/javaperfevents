# Java Perf Events

Java wrapper for Linux `perf` tool/api

## Usage

### Single event
```
    PerfEvent cycles = new PerfEvent("cycles");
    cycles.start();
    try {
        // some code we want to measure
        System.out.printf("%s: %,d\n", cycles.getEventName, cycles.read());
    } finally {
        cycles.shutdown();
    }
```

### Multiple events
```
    PerfEvent events = new PerfGroupEvent("cycles,instructions,cache-misses");
    events.start();
    try {
        // some code we want to measure
        events.read((name, value) -> System.out.printf("%s: %,d\n", name, value));
    } finally {
        events.shutdown();
    }
```

## Events

`Java Perf Events` supports Generic event (Hardware, Hardware Cache & Software), architecture specific PMU events and Linux tracepoints
 
 ## Perf Java Wrapper
 
 `Java Perf Events` provides `JPerf` class as a perf wrapper that reimplements roughly `perf list` & `perf stat` commands. This is use as a sample of API usage 