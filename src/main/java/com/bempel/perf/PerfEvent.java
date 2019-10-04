package com.bempel.perf;

import com.bempel.perf.jna.CLibrary;
import com.bempel.perf.jna.PerfEventConsts;
import com.bempel.perf.jna.PerfEventAttr;
import com.sun.jna.Native;
import com.sun.jna.ptr.LongByReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.function.ObjLongConsumer;

/**
 * Wraps a perf event or a group of perf events
 * <br>
 * Usage:
 *  <pre>
 *      PerfEvent events = new PerfEvent("cycles,instructions,cache-misses");
 *      events.start();
 *      try {
 *          // some code we want to measure
 *          events.read((name, value) -> System.out.printf("%s: %d\n", name, value));
 *      } finally {
 *          events.shutdown();
 *      }
 *  </pre>
 */
public class PerfEvent {
    private static final int LONG_SIZE = 8;

    private List<EventInfo> eventInfos = new ArrayList<>();
    private int groupfd;

    /**
     * Creates perf event
     * @param eventName perf event name, supports group of events with comma-separated event name list
     */
    public PerfEvent(String eventName) {
        String[] eventNames = eventName.split(",");
        for (String name : eventNames) {
            eventInfos.add(new EventInfo(name));
        }
    }

    /**
     * Opens the event or group of events on the current process and on any cpu/core
     */
    public void open() {
        groupfd = -1;
        for (EventInfo ei : eventInfos) {
            ei.fd = openEvent(ei.attr, 0, -1, groupfd, 0);
            if (groupfd == -1) {
                groupfd = ei.fd; // store groupfd for the next perf_event_open call
            }
            ei.groupId = getGroupId(ei);
        }
    }

    /**
     * Enables the event or group of events
     */
    public void enable() {
        for (EventInfo ei : eventInfos) {
            ioctl(ei, PerfEventConsts.PERF_EVENT_IOC_ENABLE);
        }
    }

    /**
     * Resets the event or group of events
     */
    public void reset() {
        for (EventInfo ei : eventInfos) {
            ioctl(ei, PerfEventConsts.PERF_EVENT_IOC_RESET);
        }
    }

    /**
     * Disables the event or group of events
     */
    public void disable() {
        for (EventInfo ei : eventInfos) {
            ioctl(ei, PerfEventConsts.PERF_EVENT_IOC_DISABLE);
        }
    }

    /**
     * Reads value of an event or a group of events and performs an action on each event
     * @param reader action performed on each event read
     */
    public void read(ObjLongConsumer<String> reader) {
        // from read_format: n events * (value + groupid) + nr
        int totalValues = eventInfos.size() * 2 + 1;
        long[] valueBuffer = new long[totalValues];
        int ret = CLibrary.INSTANCE.read(groupfd, valueBuffer, totalValues * LONG_SIZE);
        if (ret < 0) {
            int errno = Native.getLastError();
            String msg = String.format("Cannot perform read on groupfd[%d]: %s", groupfd, CLibrary.INSTANCE.strerror(errno));
            throw new UnsupportedOperationException(msg);
        }
        int idx = 0;
        int max = ret / LONG_SIZE;
        // first cell in valueBuffer is the number of values
        long nr = valueBuffer[idx++];
        if (max != (nr * 2 + 1))
            throw new IllegalStateException(String.format("Error reading event buffer read [%d] and Number of events [%d] mismatched", max, nr));
        while (idx < max) {
            long value = valueBuffer[idx++];
            if (idx >= max) {
                throw new IllegalStateException(String.format("Error reading event buffer read, out of bounds idx[%d] max[%d]", idx, max));
            }
            long groupId = valueBuffer[idx++];
            for (EventInfo ei : eventInfos) {
                if (ei.groupId == groupId) {
                    reader.accept(ei.eventName, value);
                }
            }
        }
    }

    /**
     * Closes the event or group of events
     * To be able to use it again, needs to perform open
     */
    public void close() {
        for (EventInfo ei : eventInfos) {
            int ret = CLibrary.INSTANCE.close(ei.fd);
            if (ret < 0) {
                int errno = Native.getLastError();
                String msg = String.format("Cannot perform close on fd[%d]: %s", ei.fd, CLibrary.INSTANCE.strerror(errno));
                throw new UnsupportedOperationException(msg);
            }
        }
    }

    /**
     * Starts the event or group of events by performing open/reset/enable operations
     */
    public void start() {
        open();
        reset();
        enable();
    }

    /**
     * Shutdown the event or group of events by performing disable/close operations
     * To be able to use it again, needs to perform open or start
     */
    public void shutdown() {
        disable();
        close();
    }

    private void ioctl(EventInfo ei, int ioctlOp) {
        int ret = CLibrary.INSTANCE.ioctl(ei.fd, ioctlOp, 0);
        if (ret < 0) {
            int errno = Native.getLastError();
            String msg = String.format("Cannot perform ioctl on fd[%d]: %s", ei.fd, CLibrary.INSTANCE.strerror(errno));
            throw new UnsupportedOperationException(msg);
        }
    }

    private long getGroupId(EventInfo ei) {
        LongByReference id = new LongByReference();
        int ret = CLibrary.INSTANCE.ioctl(ei.fd, PerfEventConsts.PERF_EVENT_IOC_ID, id);
        if (ret < 0) {
            int errno = Native.getLastError();
            String msg = String.format("Cannot perform ioctl PERF_EVENT_IOC_ID on fd[%d]: %s", ei.fd, CLibrary.INSTANCE.strerror(errno));
            throw new UnsupportedOperationException(msg);
        }
        return id.getValue();
    }

    private static int openEvent(PerfEventAttr attr, int pid, int cpu, int group_fd, int flags) {
        int fd = CLibrary.INSTANCE.syscall(PerfEventConsts.PERF_EVENT_OPEN, attr, pid, cpu, group_fd, flags);
        if (fd == -1) {
            int errno = Native.getLastError();
            String msg = String.format("Cannot open perf event type[%d] event[%d]: %s", attr.type, attr.config, CLibrary.INSTANCE.strerror(errno));
            throw new UnsupportedOperationException(msg);
        }
        return fd;
    }

    private static class EventInfo {
        private PerfEventAttr attr = new PerfEventAttr();
        private int fd;
        private long groupId;
        private String eventName;

        public EventInfo(String eventName) {
            this.eventName = eventName;
            attr.flags = PerfEventAttr.DISABLED | PerfEventAttr.EXCLUDE_KERNEL | PerfEventAttr.EXCLUDE_HV;
            attr.read_format = PerfEventConsts.PERF_FORMAT_GROUP | PerfEventConsts.PERF_FORMAT_ID;
            switch (eventName) {
                case "branch-instructions":
                case "branches":
                    attr.type = PerfEventConsts.PERF_TYPE_HARDWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_HW_BRANCH_INSTRUCTIONS;
                    break;
                case "branch-misses":
                    attr.type = PerfEventConsts.PERF_TYPE_HARDWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_HW_BRANCH_MISSES;
                    break;
                case "bus-cycles":
                    attr.type = PerfEventConsts.PERF_TYPE_HARDWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_HW_BUS_CYCLES;
                    break;
                case "cache-misses":
                    attr.type = PerfEventConsts.PERF_TYPE_HARDWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_HW_CACHE_MISSES;
                    break;
                case "cache-references":
                    attr.type = PerfEventConsts.PERF_TYPE_HARDWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_HW_CACHE_REF;
                    break;
                case "cycles":
                case "cpu-cycles":
                    attr.type = PerfEventConsts.PERF_TYPE_HARDWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_HW_CPU_CYCLES;
                    break;
                case "instructions":
                    attr.type = PerfEventConsts.PERF_TYPE_HARDWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_HW_INSTRUCTIONS;
                    break;
                case "ref-cycles":
                    attr.type = PerfEventConsts.PERF_TYPE_HARDWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_HW_REF_CPU_CYCLES;
                    break;
                case "alignment-faults":
                    attr.type = PerfEventConsts.PERF_TYPE_SOFTWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_SW_ALIGNMENT_FAULTS;
                    break;
                case "bpf-output":
                    attr.type = PerfEventConsts.PERF_TYPE_SOFTWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_SW_BPF_OUTPUT;
                    break;
                case "context-switches":
                case "cs":
                    attr.type = PerfEventConsts.PERF_TYPE_SOFTWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_SW_CONTEXT_SWITCHES;
                    break;
                case "cpu-clock":
                    attr.type = PerfEventConsts.PERF_TYPE_SOFTWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_SW_CPU_CLOCK;
                    break;
                case "cpu-migrations":
                case "migrations":
                    attr.type = PerfEventConsts.PERF_TYPE_SOFTWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_SW_CPU_MIGRATIONS;
                    break;
                case "dummy":
                    attr.type = PerfEventConsts.PERF_TYPE_SOFTWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_SW_DUMMY;
                    break;
                case "emulation-faults":
                    attr.type = PerfEventConsts.PERF_TYPE_SOFTWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_SW_EMULATION_FAULTS;
                    break;
                case "major-faults":
                    attr.type = PerfEventConsts.PERF_TYPE_SOFTWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_SW_PAGE_FAULTS_MAJ;
                    break;
                case "mminor-faults":
                    attr.type = PerfEventConsts.PERF_TYPE_SOFTWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_SW_PAGE_FAULTS_MIN;
                    break;
                case "page-faults":
                case "faults":
                    attr.type = PerfEventConsts.PERF_TYPE_SOFTWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_SW_PAGE_FAULTS;
                    break;
                case "task-clock":
                    attr.type = PerfEventConsts.PERF_TYPE_SOFTWARE;
                    attr.config = PerfEventConsts.PERF_COUNT_SW_TASK_CLOCK;
                    break;
                case "L1-dcache-load-misses":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_L1D,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_READ,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_MISS);
                    break;
                case "L1-dcache-loads":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_L1D,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_READ,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_ACCESS);
                    break;
                case "L1-dcache-store-misses":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_L1D,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_WRITE,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_MISS);
                    break;
                case "L1-dcache-stores":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_L1D,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_WRITE,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_ACCESS);
                    break;
                case "L1-icache-load-misses":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_L1I,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_READ,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_MISS);
                    break;
                case "LLC-load-misses":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_LL,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_READ,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_MISS);
                    break;
                case "LLC-loads":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_LL,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_READ,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_ACCESS);
                    break;
                case "LLC-store-misses":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_LL,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_WRITE,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_MISS);
                    break;
                case "LLC-stores":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_LL,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_WRITE,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_ACCESS);
                    break;
                case "branch-load-misses":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_BPU,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_READ,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_MISS);
                    break;
                case "branch-loads":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_BPU,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_READ,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_ACCESS);
                    break;
                case "dTLB-load-misses":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_DTLB,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_READ,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_MISS);
                    break;
                case "dTLB-loads":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_DTLB,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_READ,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_ACCESS);
                    break;
                case "dTLB-store-misses":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_DTLB,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_WRITE,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_MISS);
                    break;
                case "dTLB-stores":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_DTLB,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_WRITE,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_ACCESS);
                    break;
                case "iTLB-load-misses":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_ITLB,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_READ,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_MISS);
                    break;
                case "iTLB-loads":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_ITLB,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_READ,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_ACCESS);
                    break;
                case "node-load-misses":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_NODE,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_READ,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_MISS);
                    break;
                case "node-loads":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_NODE,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_READ,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_ACCESS);
                    break;
                case "node-store-misses":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_NODE,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_WRITE,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_MISS);
                    break;
                case "node-stores":
                    attr.type = PerfEventConsts.PERF_TYPE_HW_CACHE;
                    attr.config = getCacheEvent(PerfEventConsts.PERF_COUNT_HW_CACHE_NODE,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_OP_WRITE,
                            PerfEventConsts.PERF_COUNT_HW_CACHE_RESULT_ACCESS);
                    break;
                default:
                    attr.type = -1;
                    attr.config = -1;
                    throw new IllegalArgumentException("Unsupported event name: " + eventName);
            }
        }

        private static int getCacheEvent(int perf_hw_cache_id, int perf_hw_cache_op_id, int perf_hw_cache_op_result_id) {
            return perf_hw_cache_id | (perf_hw_cache_op_id << 8) | (perf_hw_cache_op_result_id << 16);
        }
    }
}

