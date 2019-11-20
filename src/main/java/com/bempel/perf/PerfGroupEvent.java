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
import com.sun.jna.Native;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ObjLongConsumer;

/**
 * Wraps a perf event or a group of perf events
 * <br>
 * Usage:
 *  <pre>
 *      PerfEvent events = new PerfGroupEvent("cycles,instructions,cache-misses");
 *      events.start();
 *      try {
 *          // some code we want to measure
 *          events.read((name, value) -> System.out.printf("%s: %,d\n", name, value));
 *      } finally {
 *          events.shutdown();
 *      }
 *  </pre>
 */
public class PerfGroupEvent {
    private List<PerfEvent.EventInfo> eventInfos = new ArrayList<>();
    private int groupfd;

    /**
     * Creates perf group event
     *
     * @param eventName perf event name, supports group of events with comma-separated event name list
     */
    public PerfGroupEvent(String eventName) {
        String[] eventNames = eventName.split(",");
        for (String name : eventNames) {
            eventInfos.add(new PerfEvent.EventInfo(name));
        }
    }

    /**
     * Opens the group of events on the calling process/thread and on any cpu/core
     */
    public void open() {
        open(0, -1);
    }

    /**
     * Opens the group of events
     * @param pid indicates the pid of the process we want to measure. 0 for current process
     * @param cpu indicates the specific cpu we want to measure. -1 for all cpus
     * Note: pid == -1 && cpu == -1 is invalid
     */
    public void open(int pid, int cpu) {
        groupfd = -1;
        for (PerfEvent.EventInfo ei : eventInfos) {
            ei.fd = PerfEvent.openEvent(ei.attr, pid, cpu, groupfd, 0);
            if (groupfd == -1) {
                groupfd = ei.fd; // store groupfd for the next perf_event_open call
            }
            ei.groupId = PerfEvent.getGroupId(ei);
        }

    }

    /**
     * Enables the event or group of events
     */
    public void enable() {
        for (PerfEvent.EventInfo ei : eventInfos) {
            PerfEvent.ioctl(ei, PerfEventConsts.PERF_EVENT_IOC_ENABLE);
        }
    }
    /**
     * Resets the group of events
     */
    public void reset() {
        for (PerfEvent.EventInfo ei : eventInfos) {
            PerfEvent.ioctl(ei, PerfEventConsts.PERF_EVENT_IOC_RESET);
        }
    }

    /**
     * Disables the group of events
     */
    public void disable() {
        for (PerfEvent.EventInfo ei : eventInfos) {
            PerfEvent.ioctl(ei, PerfEventConsts.PERF_EVENT_IOC_DISABLE);
        }
    }

    /**
     * Reads value of a group of events and performs an action on each event
     * @param reader action performed on each event read
     */
    public void read(ObjLongConsumer<String> reader) {
        // from read_format: n events * (value + groupid) + nr
        int totalValues = eventInfos.size() * 2 + 1;
        long[] valueBuffer = new long[totalValues];
        int max = PerfEvent.readBuffer(groupfd, valueBuffer, totalValues);
        int idx = 1;
        while (idx < max) {
            long value = valueBuffer[idx++];
            if (idx >= max) {
                throw new IllegalStateException(String.format("Error reading event buffer read, out of bounds idx[%d] max[%d]", idx, max));
            }
            long groupId = valueBuffer[idx++];
            for (PerfEvent.EventInfo ei : eventInfos) {
                if (ei.groupId == groupId) {
                    reader.accept(ei.eventName, value);
                }
            }
        }
    }

    /**
     * Reads value of a group of events and fill the array
     * @param values long array allocated to receive the values read from events
     *               should be large enough to read all events
     */
    public void read(long[] values) {
        // from read_format: n events * (value + groupid) + nr
        int totalValues = eventInfos.size() * 2 + 1;
        if (values.length < eventInfos.size()) {
            throw new IllegalArgumentException("values is not large enough. should be at least " + eventInfos.size());
        }
        long[] valueBuffer = new long[totalValues];
        read(values, valueBuffer);
    }

    /**
     * /!\ Expert only /!\
     * Reads value of a group of events and fill the array
     * @param values long array allocated to receive the values read from events
     *               should be large enough to read all events
     * @param buffer pre-allocated buffer to perform read
     * Optimized version to avoid too much allocations
     */
    public void read(long[] values, long[] buffer) {
        int max = PerfEvent.readBuffer(groupfd, buffer, buffer.length);
        int idx = 1;
        int valueIdx = 0;
        while (idx < max) {
            long value = buffer[idx];
            values[valueIdx++] = value;
            idx += 2;
        }
    }

    /**
     * Closes the group of events
     * To be able to use it again, needs to perform open
     */
    public void close() {
        for (PerfEvent.EventInfo ei : eventInfos) {
            int ret = CLibrary.INSTANCE.close(ei.fd);
            if (ret < 0) {
                int errno = Native.getLastError();
                String msg = String.format("Cannot perform close on fd[%d]: %s", ei.fd, CLibrary.INSTANCE.strerror(errno));
                throw new UnsupportedOperationException(msg);
            }
        }
    }

    /**
     * Starts the group of events by performing open/reset/enable operations
     * for the calling process/thread for any cpu
     */
    public void start() {
        start(0, -1);
    }

    /**
     * Starts the group of events by performing open/reset/enable operations
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
     * Shutdown the group of events by performing disable/close operations
     * To be able to use it again, needs to perform open or start
     */
    public void shutdown() {
        disable();
        close();
    }
}
