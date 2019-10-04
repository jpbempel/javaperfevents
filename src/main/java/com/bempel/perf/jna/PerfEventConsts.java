package com.bempel.perf.jna;

/**
 * Perf constants (from linux/perf_event.h)
 */
public class PerfEventConsts {
    // attr.type perf_type_id
    public static final int PERF_TYPE_HARDWARE = 0;
    public static final int PERF_TYPE_SOFTWARE = 1;
    public static final int PERF_TYPE_TRACEPOINT = 2;
    public static final int PERF_TYPE_HW_CACHE = 3;
    public static final int PERF_TYPE_RAW = 4;
    public static final int PERF_TYPE_BREAKPOINT = 5;

    // perf_hw_id
    // Common hardware events, generalized by the kernel
    public static final int PERF_COUNT_HW_CPU_CYCLES = 0;
    public static final int PERF_COUNT_HW_INSTRUCTIONS = 1;
    public static final int PERF_COUNT_HW_CACHE_REF = 2;
    public static final int PERF_COUNT_HW_CACHE_MISSES = 3;
    public static final int PERF_COUNT_HW_BRANCH_INSTRUCTIONS = 4;
    public static final int PERF_COUNT_HW_BRANCH_MISSES = 5;
    public static final int PERF_COUNT_HW_BUS_CYCLES = 6;
    public static final int PERF_COUNT_HW_STALLED_CYCLES_FRONTEND = 7;
    public static final int PERF_COUNT_HW_STALLED_CYCLES_BACKEND = 8;
    public static final int PERF_COUNT_HW_REF_CPU_CYCLES = 9;

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

    // Special "software" events provided by the kernel, even if the hardware does not support performance events.
    // perf_sw_ids
    public static final int PERF_COUNT_SW_CPU_CLOCK = 0;
    public static final int PERF_COUNT_SW_TASK_CLOCK = 1;
    public static final int PERF_COUNT_SW_PAGE_FAULTS = 2;
    public static final int PERF_COUNT_SW_CONTEXT_SWITCHES = 3;
    public static final int PERF_COUNT_SW_CPU_MIGRATIONS = 4;
    public static final int PERF_COUNT_SW_PAGE_FAULTS_MIN = 5;
    public static final int PERF_COUNT_SW_PAGE_FAULTS_MAJ = 6;
    public static final int PERF_COUNT_SW_ALIGNMENT_FAULTS = 7;
    public static final int PERF_COUNT_SW_EMULATION_FAULTS = 8;
    public static final int PERF_COUNT_SW_DUMMY = 9;
    public static final int PERF_COUNT_SW_BPF_OUTPUT = 10;

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
}

