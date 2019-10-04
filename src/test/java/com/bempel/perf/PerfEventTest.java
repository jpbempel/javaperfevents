package com.bempel.perf;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

public class PerfEventTest {

    @Test
    public void start_read_shutdown() {
        PerfEvent cycles = new PerfEvent("cycles");
        cycles.start();
        try {
            cycles.read((name, value) -> {
                assertTrue(value > 0);
                assertSame("cycles", name);
            });
        } finally {
            cycles.shutdown();
        }
    }

    @Test
    public void reset() {
        PerfEvent instructions = new PerfEvent("instructions");
        instructions.start();
        try {
            int i = 0;
            while (i < 1_000_000)
                i++;
            assertEquals(1_000_000, i);
            AtomicLong previous = new AtomicLong(-1);
            instructions.read((name, value) -> {
                assertTrue(value > 0);
                assertEquals("instructions", name);
                previous.set(value);
            });
            instructions.reset();
            instructions.read((name, value) -> {
                assertTrue(value > 0);
                assertEquals("instructions", name);
                assertTrue(previous.get() > value);
            });
        } finally {
            instructions.shutdown();
        }
    }

    @Test
    public void disable() {
        PerfEvent instructions = new PerfEvent("instructions");
        instructions.start();
        try {
            int i = 0;
            while (i < 1_000_000)
                i++;
            assertEquals(1_000_000, i);
            instructions.read((name, value) -> {
                assertTrue(value > 0);
                assertEquals("instructions", name);
            });
            instructions.disable();
            instructions.reset();
            instructions.read((name, value) -> {
                assertEquals(0, value);
                assertEquals("instructions", name);
            });
        } finally {
            instructions.shutdown();
        }
    }

    @Test
    public void eventGroup() {
        List<Long> values = new ArrayList<>();
        List<String> names = new ArrayList<>();
        PerfEvent events = new PerfEvent("cycles,instructions");
        events.start();
        try {
            events.read((name, value) -> {
                names.add(name);
                values.add(value);
            });
            assertEquals(2, names.size());
            assertEquals(2, values.size());
            assertEquals("cycles", names.get(0));
            assertEquals("instructions", names.get(1));
            assertTrue(values.get(0) > 0);
            assertTrue(values.get(1) > 0);
        } finally {
            events.shutdown();
        }
    }

    @Test
    public void start_read_shutdown_start_read_shutdown() {
        PerfEvent cycles = new PerfEvent("cycles");
        cycles.start();
        try {
            cycles.read((name, value) -> {
                assertTrue(value > 0);
                assertSame("cycles", name);
            });
        } finally {
            cycles.shutdown();
        }
        cycles.start();
        try {
            cycles.read((name, value) -> {
                assertTrue(value > 0);
                assertSame("cycles", name);
            });
        } finally {
            cycles.shutdown();
        }
    }

}