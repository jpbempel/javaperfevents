package com.bempel.perf.jna;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * Mapping of struct perf_event_attr (from linux/perf_event.h)
 */
public class PerfEventAttr extends Structure implements Structure.ByReference {
    // Bit flags
    public static final long DISABLED = 1 << 0;
    public static final long INHERIT = 1 << 1;
    public static final long PINNED = 1 << 2;
    public static final long EXCLUSIVE = 1 << 3;
    public static final long EXCLUDE_USER = 1 << 4;
    public static final long EXCLUDE_KERNEL = 1 << 5;
    public static final long EXCLUDE_HV = 1 << 6;
    public static final long EXCLUDE_IDLE = 1 << 7;
    public static final long MMAP = 1 << 8;
    public static final long COMM = 1 << 9;
    public static final long FREQ = 1 << 10;
    public static final long INHERIT_STAT = 1 << 11;
    public static final long ENABLE_ON_EXEC = 1 << 12;
    public static final long TASK = 1 << 13;
    public static final long WATERMARK = 1 << 14;
    public static final long PRECISE_IP1 = 1 << 15;
    public static final long PRECISE_IP2 = 1 << 16;
    public static final long MMAP_DATA = 1 << 17;
    public static final long SAMPLE_ID_ALL = 1 << 18;
    public static final long EXCLUDE_HOST = 1 << 19;
    public static final long EXCLUDE_GUEST = 1 << 20;
    public static final long EXCLUDE_CALLCHAIN_KERNEL = 1 << 21;
    public static final long EXCLUDE_CALLCHAIN_USER = 1 << 22;
    public static final long MMAP2 = 1 << 23;
    public static final long COMM_EXEC = 1 << 24;
    public static final long USE_CLOCKID = 1 << 25;
    public static final long CONTEXT_SWITCH = 1 << 26;
    public static final long RESERVED_1 = 1 << 27;

    private static final int PERF_ATTR_SIZE_VER5 = 112;

    public int type;
    public int size = PERF_ATTR_SIZE_VER5;
    public long config;
    public long sample_period;
    public long sample_type;
    public long read_format;
    public long flags;
    public int wakeup_events;
    public int bp_type;
    public long bp_addr;
    public long bp_len;
    public long branch_sample_type;
    public long sample_regs_user;
    public int sample_stack_user;
    public int clockid;
    public long sample_regs_intr;
    public int aux_watermark;
    public int reserved_2;

    protected List<String> getFieldOrder() {
        return Arrays.asList(
                "type",
                "size",
                "config",
                "sample_period",
                "sample_type",
                "read_format",
                "flags",
                "wakeup_events",
                "bp_type",
                "bp_addr",
                "bp_len",
                "branch_sample_type",
                "sample_regs_user",
                "sample_stack_user",
                "clockid",
                "sample_regs_intr",
                "aux_watermark",
                "reserved_2"
        );
    }
}
