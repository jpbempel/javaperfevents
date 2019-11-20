/*
 * Copyright 2019 Jean-Philippe Bempel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bempel.perf;

import com.bempel.perf.jna.CLibrary;
import com.bempel.perf.jna.PerfEventConsts;
import com.bempel.perf.jna.PerfEventAttr;
import com.bempel.perf.jna.Tracepoint;
import com.bempel.perf.pmuevents.PMUEvent;
import com.bempel.perf.pmuevents.PMUEvents;
import com.sun.jna.Native;
import com.sun.jna.ptr.LongByReference;

import java.util.function.Supplier;

/**
 * Wraps a perf event
 * <br>
 * Usage:
 *  <pre>
 *      PerfEvent cycles = new PerfEvent("cycles");
 *      cycles.start();
 *      try {
 *          // some code we want to measure
 *          System.out.printf("%s: %,d\n", cycles.getEventName, cycles.read());
 *      } finally {
 *          cycles.shutdown();
 *      }
 *  </pre>
 */
public class PerfEvent {
    static final int LONG_SIZE = 8;

    @FunctionalInterface
    public interface Logger {
        void log(Supplier<String> msg);
    }

    public static void installLogger(Logger customLogger) {
        logger = customLogger;
    }

    static Logger logger = (msg) -> {};

    private EventInfo eventInfo;

    /**
     * Creates perf event
     * @param eventName perf event name
     */
    public PerfEvent(String eventName) {
        this.eventInfo = new EventInfo(eventName);
    }

    /**
     * @return event name
     */
    public String getEventName() {
        return eventInfo.eventName;
    }

    /**
     * Opens the event on the calling process/thread and on any cpu/core
     */
    public void open() {
        open(0, -1);
    }

    /**
     * Opens the event
     * @param pid indicates the pid of the process we want to measure. 0 for current process
     * @param cpu indicates the specific cpu we want to measure. -1 for all cpus
     * Note: pid == -1 && cpu == -1 is invalid
     */
    public void open(int pid, int cpu) {
        eventInfo.fd = openEvent(eventInfo.attr, pid, cpu, -1, 0);
        eventInfo.groupId = getGroupId(eventInfo);
    }

    /**
     * Enables the event
     */
    public void enable() {
        ioctl(eventInfo, PerfEventConsts.PERF_EVENT_IOC_ENABLE);
    }

    /**
     * Resets the event
     */
    public void reset() {
        ioctl(eventInfo, PerfEventConsts.PERF_EVENT_IOC_RESET);
    }

    /**
     * Disables the event
     */
    public void disable() {
        ioctl(eventInfo, PerfEventConsts.PERF_EVENT_IOC_DISABLE);
    }

    /**
     * Reads value of an event
     */
    public long read() {
        int totalValues = 3; // 1 * 2 + 1;
        long[] buffer = new long[totalValues];
        readBuffer(eventInfo.fd, buffer, totalValues);
        return buffer[1];
    }

    /**
     * /!\ Expert only /!\
     * Reads value of an event or a group of events and fill the array
     * @param buffer pre-allocated buffer to perform read
     * Optimized version to avoid too much allocations
     */
    public long read(long[] buffer) {
        readBuffer(eventInfo.fd, buffer, buffer.length);
        return buffer[1];
    }

    /**
     * Closes the event
     * To be able to use it again, needs to perform open
     */
    public void close() {
        int ret = CLibrary.INSTANCE.close(eventInfo.fd);
        if (ret < 0) {
            int errno = Native.getLastError();
            String msg = String.format("Cannot perform close on fd[%d]: %s", eventInfo.fd, CLibrary.INSTANCE.strerror(errno));
            throw new UnsupportedOperationException(msg);
        }
    }

    /**
     * Starts the event performing open/reset/enable operations
     * for the calling process/thread for any cpu
     */
    public void start() {
        start(0, -1);
    }

    /**
     * Starts the event by performing open/reset/enable operations
     * @param pid indicates the pid of the process we want to measure. 0 for current process
     * @param cpu indicates the specific cpu we want to measure. -1 for all cpus
     * Note: pid == -1 && cpu == -1 is invalid
     */
    public void start(int pid, int cpu) {
        open(pid, cpu);
        reset();
        enable();
    }

    /**
     * Shutdown the event by performing disable/close operations
     * To be able to use it again, needs to perform open or start
     */
    public void shutdown() {
        disable();
        close();
    }

    static int readBuffer(int fd, long[] buffer, int len) {
        int ret = CLibrary.INSTANCE.read(fd, buffer, len * LONG_SIZE);
        if (ret < 0) {
            int errno = Native.getLastError();
            String msg = String.format("Cannot perform read on groupfd[%d]: %s", fd, CLibrary.INSTANCE.strerror(errno));
            throw new UnsupportedOperationException(msg);
        }
        logger.log(() -> String.format("read on fd[%d] len[%d] bytesread[%d]\n", fd, len, ret));
        int max = ret / LONG_SIZE;
        // first cell in valueBuffer is the number of values
        long nr = buffer[0];
        if (max != (nr * 2 + 1))
            throw new IllegalStateException(String.format("Error reading event buffer read [%d] and Number of events [%d] mismatched", max, nr));
        return max;
    }

    static void ioctl(EventInfo ei, int ioctlOp) {
        int ret = CLibrary.INSTANCE.ioctl(ei.fd, ioctlOp, 0);
        if (ret < 0) {
            int errno = Native.getLastError();
            String msg = String.format("Cannot perform ioctl on fd[%d]: %s", ei.fd, CLibrary.INSTANCE.strerror(errno));
            throw new UnsupportedOperationException(msg);
        }
        logger.log(() -> String.format("Performing ioctl[%x] on fd[%d]\n", ioctlOp, ei.fd));
    }

    static long getGroupId(EventInfo ei) {
        LongByReference id = new LongByReference();
        int ret = CLibrary.INSTANCE.ioctl(ei.fd, PerfEventConsts.PERF_EVENT_IOC_ID, id);
        if (ret < 0) {
            int errno = Native.getLastError();
            String msg = String.format("Cannot perform ioctl PERF_EVENT_IOC_ID on fd[%d]: %s", ei.fd, CLibrary.INSTANCE.strerror(errno));
            throw new UnsupportedOperationException(msg);
        }
        logger.log(() -> String.format("getGroupdId on fd[%d]: %d\n", ei.fd, id.getValue()));
        return id.getValue();
    }

    static int openEvent(PerfEventAttr attr, int pid, int cpu, int group_fd, int flags) {
        int fd = CLibrary.INSTANCE.syscall(PerfEventConsts.PERF_EVENT_OPEN, attr, pid, cpu, group_fd, flags);
        if (fd == -1) {
            int errno = Native.getLastError();
            String msg = String.format("Cannot open perf event type[%d] event[%d]: %s", attr.type, attr.config, CLibrary.INSTANCE.strerror(errno));
            throw new UnsupportedOperationException(msg);
        }
        logger.log(() -> String.format("openEvent type[%d] event[%d] pid[%d] cpu[%d] groupfd[%d] => fd[%d]\n", attr.type, attr.config, pid, cpu, group_fd, fd));
        return fd;
    }

    static class EventInfo {
        final PerfEventAttr attr = new PerfEventAttr();
        int fd;
        long groupId;
        final String eventName;

        public EventInfo(String eventName) {
            this.eventName = eventName;
            attr.flags = PerfEventAttr.DISABLED | PerfEventAttr.EXCLUDE_KERNEL | PerfEventAttr.EXCLUDE_HV;
            attr.read_format = PerfEventConsts.PERF_FORMAT_GROUP | PerfEventConsts.PERF_FORMAT_ID;
            initPerfEventByName(attr, eventName);
        }

        static void initPerfEventByName(PerfEventAttr ea, String lookupName) {
            // lookup in predefined generic perf events
            PerfEventConsts.PerfEventInfo perfEventInfo = PerfEventConsts.getPerfEvent(lookupName);
            if (perfEventInfo != null) {
                ea.type = perfEventInfo.getType();
                ea.config = perfEventInfo.getValue();
                if (perfEventInfo.requiresKernel()) {
                    ea.flags &= ~PerfEventAttr.EXCLUDE_KERNEL;
                }
                return;
            }
            // lookup in PMU events
            PMUEvent pmuEvent = PMUEvents.getPMUEventMap().get(lookupName);
            if (pmuEvent != null) {
                ea.type = PerfEventConsts.PERF_TYPE_RAW;
                int eventCode = Integer.decode(pmuEvent.eventCode);
                int umask = Integer.decode(pmuEvent.umask);
                ea.config = umask << 8 | eventCode;
                return;
            }
            // lookup in Tracepoints
            Tracepoint.TracepointInfo tracepointInfo = Tracepoint.get(lookupName);
            if (tracepointInfo != null) {
                ea.type = PerfEventConsts.PERF_TYPE_TRACEPOINT;
                ea.config = tracepointInfo.getId();
                return;
            }
            throw new IllegalArgumentException("Cannot find perf event: " + lookupName);
        }
    }
}

