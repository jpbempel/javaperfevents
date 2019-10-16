package com.bempel.perf.jna;

import java.util.HashMap;
import java.util.Map;

/**
 * Perf constants (from linux/perf_event.h)
 */
public class PerfEventConsts {
    private static final Map<String, PerfEventInfo> perfEventMap = new HashMap<>();
    static {
        // pre touch to initialize perfEventMap
        PerfHwId.init();
        PerfHwCacheId.init();
        PerfSwId.init();
    }

    public static PerfEventInfo getPerfEvent(String name) {
        return perfEventMap.get(name.toUpperCase());
    }

    // attr.type perf_type_id
    public static final int PERF_TYPE_HARDWARE = 0;
    public static final int PERF_TYPE_SOFTWARE = 1;
    public static final int PERF_TYPE_TRACEPOINT = 2;
    public static final int PERF_TYPE_HW_CACHE = 3;
    public static final int PERF_TYPE_RAW = 4;
    public static final int PERF_TYPE_BREAKPOINT = 5;

    // perf_hw_id
    // Common hardware events, generalized by the kernel
    public enum PerfHwId implements PerfEventInfo {
        PERF_COUNT_HW_CPU_CYCLES(0, "CPU-CYCLES", "CYCLES"),
        PERF_COUNT_HW_INSTRUCTIONS(1, "INSTRUCTIONS"),
        PERF_COUNT_HW_CACHE_REF(2, "CACHE-REFERENCES"),
        PERF_COUNT_HW_CACHE_MISSES(3, "CACHE-MISSES"),
        PERF_COUNT_HW_BRANCH_INSTRUCTIONS(4, "BRANCH-INSTRUCTIONS", "BRANCHES"),
        PERF_COUNT_HW_BRANCH_MISSES(5, "BRANCH-MISSES"),
        PERF_COUNT_HW_BUS_CYCLES(6, "BUS-CYCLES"),
        PERF_COUNT_HW_STALLED_CYCLES_FRONTEND(7, "STALLED-CYCLES-FRONTEND"),
        PERF_COUNT_HW_STALLED_CYCLES_BACKEND(8, "STALLED-CYCLES-BACKEND"),
        PERF_COUNT_HW_REF_CPU_CYCLES(9, "REF-CYCLES");

        private final int value;
        private final String name;

        PerfHwId(int value, String... names) {
            this.value = value;
            if (names.length == 0) {
                throw new IllegalArgumentException("Should at have a name");
            }
            this.name = names[0];
            for (String alias : names) {
                perfEventMap.put(alias.toUpperCase(), this);
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public int getType() {
            return PERF_TYPE_HARDWARE;
        }

        @Override
        public boolean requiresKernel() {
            return false;
        }

        static void init() { }

        public static String toString(int value) {
            if (value < 0 || value >= values().length) {
                throw new RuntimeException("Invalid value for PerfHwId: " + value);
            }
            return values()[value].name;
        }
    }

    // Generalized hardware cache events
    // { L1-D, L1-I, LLC, ITLB, DTLB, BPU, NODE } x { read, write, prefetch } x { accesses, misses }
    // perf_hw_cache_id
    public static final int PERF_COUNT_HW_CACHE_L1D = 0;
    public static final int PERF_COUNT_HW_CACHE_L1I = 1;
    public static final int PERF_COUNT_HW_CACHE_LL = 2;
    public static final int PERF_COUNT_HW_CACHE_DTLB = 3;
    public static final int PERF_COUNT_HW_CACHE_ITLB = 4;
    public static final int PERF_COUNT_HW_CACHE_BPU = 5;
    public static final int PERF_COUNT_HW_CACHE_NODE = 6;
    // perf_hw_cache_op_id
    public static final int PERF_COUNT_HW_CACHE_OP_READ = 0;
    public static final int PERF_COUNT_HW_CACHE_OP_WRITE = 1;
    public static final int PERF_COUNT_HW_CACHE_OP_PREFETCH = 2;
    // perf_hw_cache_op_result_id
    public static final int PERF_COUNT_HW_CACHE_RESULT_ACCESS = 0;
    public static final int PERF_COUNT_HW_CACHE_RESULT_MISS = 1;

    public enum PerfHwCacheId implements PerfEventInfo {
        L1_DCACHE_LOAD_MISSES(PERF_COUNT_HW_CACHE_L1D, PERF_COUNT_HW_CACHE_OP_READ, PERF_COUNT_HW_CACHE_RESULT_MISS,"L1-DCACHE-LOAD-MISSES"),
        L1_DCACHE_LOADS(PERF_COUNT_HW_CACHE_L1D, PERF_COUNT_HW_CACHE_OP_READ, PERF_COUNT_HW_CACHE_RESULT_ACCESS,"L1-DCACHE-LOADS"),
        L1_DCACHE_STORE_MISSES(PERF_COUNT_HW_CACHE_L1D, PERF_COUNT_HW_CACHE_OP_WRITE, PERF_COUNT_HW_CACHE_RESULT_MISS, "L1-DCACHE-STORE-MISSES"),
        L1_DCACHE_STORES(PERF_COUNT_HW_CACHE_L1D, PERF_COUNT_HW_CACHE_OP_WRITE, PERF_COUNT_HW_CACHE_RESULT_ACCESS, "L1-DCACHE-STORES"),
        L1_ICACHE_LOAD_MISSES(PERF_COUNT_HW_CACHE_L1I, PERF_COUNT_HW_CACHE_OP_READ, PERF_COUNT_HW_CACHE_RESULT_MISS, "L1-ICACHE-LOAD-MISSES"),
        LLC_LOAD_MISSES(PERF_COUNT_HW_CACHE_LL, PERF_COUNT_HW_CACHE_OP_READ, PERF_COUNT_HW_CACHE_RESULT_MISS, "LLC-LOAD-MISSES"),
        LLC_LOADS(PERF_COUNT_HW_CACHE_LL, PERF_COUNT_HW_CACHE_OP_READ, PERF_COUNT_HW_CACHE_RESULT_ACCESS, "LLC-LOADS"),
        LLC_STORE_MISSES(PERF_COUNT_HW_CACHE_LL, PERF_COUNT_HW_CACHE_OP_WRITE, PERF_COUNT_HW_CACHE_RESULT_MISS, "LLC-STORE-MISSES"),
        LLC_STORES(PERF_COUNT_HW_CACHE_LL, PERF_COUNT_HW_CACHE_OP_WRITE, PERF_COUNT_HW_CACHE_RESULT_ACCESS, "LLC-STORES"),
        BRANCH_LOAD_MISSES(PERF_COUNT_HW_CACHE_BPU, PERF_COUNT_HW_CACHE_OP_READ, PERF_COUNT_HW_CACHE_RESULT_MISS, "BRANCH-LOAD-MISSES"),
        BRANCH_LOADS(PERF_COUNT_HW_CACHE_BPU, PERF_COUNT_HW_CACHE_OP_READ, PERF_COUNT_HW_CACHE_RESULT_ACCESS, "BRANCH-LOADS"),
        DTLB_LOAD_MISSES(PERF_COUNT_HW_CACHE_DTLB, PERF_COUNT_HW_CACHE_OP_READ, PERF_COUNT_HW_CACHE_RESULT_MISS, "DTLB-LOAD-MISSES"),
        DTLB_LOADS(PERF_COUNT_HW_CACHE_DTLB, PERF_COUNT_HW_CACHE_OP_READ, PERF_COUNT_HW_CACHE_RESULT_ACCESS, "DTLB-LOADS"),
        DTLB_STORE_MISSES(PERF_COUNT_HW_CACHE_DTLB, PERF_COUNT_HW_CACHE_OP_WRITE, PERF_COUNT_HW_CACHE_RESULT_MISS, "DTLB-STORE-MISSES"),
        DTLB_STORES(PERF_COUNT_HW_CACHE_DTLB, PERF_COUNT_HW_CACHE_OP_WRITE, PERF_COUNT_HW_CACHE_RESULT_ACCESS, "DTLB-STORES"),
        ITLB_LOAD_MISSES(PERF_COUNT_HW_CACHE_ITLB, PERF_COUNT_HW_CACHE_OP_READ, PERF_COUNT_HW_CACHE_RESULT_MISS, "ITLB-LOAD-MISSES"),
        ITLB_LOADS(PERF_COUNT_HW_CACHE_ITLB, PERF_COUNT_HW_CACHE_OP_READ, PERF_COUNT_HW_CACHE_RESULT_ACCESS, "ITLB-LOADS"),
        NODE_LOAD_MISSES(PERF_COUNT_HW_CACHE_NODE, PERF_COUNT_HW_CACHE_OP_READ, PERF_COUNT_HW_CACHE_RESULT_MISS, "NODE-LOAD-MISSES"),
        NODE_LOADS(PERF_COUNT_HW_CACHE_NODE, PERF_COUNT_HW_CACHE_OP_READ, PERF_COUNT_HW_CACHE_RESULT_ACCESS, "NODE-LOADS"),
        NODE_STORE_MISSES(PERF_COUNT_HW_CACHE_NODE, PERF_COUNT_HW_CACHE_OP_WRITE, PERF_COUNT_HW_CACHE_RESULT_MISS, "NODE-STORE-MISSES"),
        NODE_STORES(PERF_COUNT_HW_CACHE_NODE, PERF_COUNT_HW_CACHE_OP_WRITE, PERF_COUNT_HW_CACHE_RESULT_ACCESS, "NODE-STORES");

        private final int value;
        private final String name;

        PerfHwCacheId(int cacheId, int cacheOpId, int cacheResultId, String... names) {
            this.value = cacheId | (cacheOpId << 8) | (cacheResultId << 16);
            if (names.length == 0) {
                throw new IllegalArgumentException("Should at have a name");
            }
            this.name = names[0];
            for (String alias : names) {
                perfEventMap.put(alias.toUpperCase(), this);
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public int getType() {
            return PERF_TYPE_HARDWARE;
        }

        @Override
        public boolean requiresKernel() {
            return false;
        }

        static void init() { }

        public static String toString(int value) {
            if (value < 0 || value >= values().length) {
                throw new RuntimeException("Invalid value for PerfHwCacheId: " + value);
            }
            return values()[value].name;
        }
    }

    // Special "software" events provided by the kernel, even if the hardware does not support performance events.
    // perf_sw_ids
    public enum PerfSwId implements PerfEventInfo {
        PERF_COUNT_SW_CPU_CLOCK(0, false, "CPU-CLOCK"),
        PERF_COUNT_SW_TASK_CLOCK(1, false, "TASK-CLOCK"),
        PERF_COUNT_SW_PAGE_FAULTS(2, false, "PAGE-FAULTS", "FAULTS"),
        PERF_COUNT_SW_CONTEXT_SWITCHES(3, true, "CONTEXT-SWITCHES", "CS"),
        PERF_COUNT_SW_CPU_MIGRATIONS(4, true, "CPU-MIGRATIONS", "MIGRATIONS"),
        PERF_COUNT_SW_PAGE_FAULTS_MIN(5, false, "MINOR-FAULTS"),
        PERF_COUNT_SW_PAGE_FAULTS_MAJ(6, false, "MAJOR-FAULTS"),
        PERF_COUNT_SW_ALIGNMENT_FAULTS(7, false, "ALIGNMENT-FAULTS"),
        PERF_COUNT_SW_EMULATION_FAULTS(8, false, "EMULATION-FAULTS"),
        PERF_COUNT_SW_DUMMY(9, false, "DUMMY"),
        PERF_COUNT_SW_BPF_OUTPUT(10, false, "BPF-OUTPUT");

        private final int value;
        private final boolean requiresKernel;
        private final String name;

        PerfSwId(int value, boolean requiresKernel, String... names) {
            this.value = value;
            this.requiresKernel = requiresKernel;
            if (names.length == 0) {
                throw new IllegalArgumentException("Should at have a name");
            }
            this.name = names[0];
            for (String alias : names) {
                perfEventMap.put(alias.toUpperCase(), this);
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public int getType() {
            return PERF_TYPE_SOFTWARE;
        }

        @Override
        public boolean requiresKernel() {
            return requiresKernel;
        }

        static void init() { }

        public static String toString(int value) {
            if (value < 0 || value >= values().length) {
                throw new RuntimeException("Invalid value for PerfSwId: " + value);
            }
            return values()[value].name;
        }
    }

    // perf_event_open syscall for x86_64
    public static final int PERF_EVENT_OPEN = 298;

    // Ioctls that can be done on a perf event fd (x86_64 specific)
    public static final int PERF_EVENT_IOC_ENABLE = 0x2400;
    public static final int PERF_EVENT_IOC_DISABLE = 0x2401;
    public static final int PERF_EVENT_IOC_REFRESH = 0x2402;
    public static final int PERF_EVENT_IOC_RESET = 0x2403;
    public static final int PERF_EVENT_IOC_PERIOD = 0x2404;
    public static final int PERF_EVENT_IOC_SET_OUTPUT = 0x2405;
    public static final int PERF_EVENT_IOC_SET_FILTER = 0x2406;
    public static final long PERF_EVENT_IOC_ID = 0x80082407;
    public static final int PERF_EVENT_IOC_SET_BPF = 0x2408;
    public static final int PERF_EVENT_IOC_PAUSE_OUTPUT = 0x2409;
    public static final int PERF_EVENT_IOC_QUERY_BPF = 0x240A;
    public static final int PERF_EVENT_IOC_MODIFY_ATTRIBUTES = 0x240B;

    // perf_event_read_format
    // The format of the data returned by read() on a perf event fd, as specified by attr.read_format
    public static final int PERF_FORMAT_TOTAL_TIME_ENABLED = 1 << 0;
    public static final int PERF_FORMAT_TOTAL_TIME_RUNNING = 1 << 1;
    public static final int PERF_FORMAT_ID = 1 << 2;
    public static final int PERF_FORMAT_GROUP = 1 << 3;

    public interface PerfEventInfo {
        String getName();
        int getValue();
        int getType();
        boolean requiresKernel();
    }
}
