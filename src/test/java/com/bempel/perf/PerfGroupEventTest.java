package com.bempel.perf;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PerfGroupEventTest {

    @Test
    public void eventGroup() {
        List<Long> values = new ArrayList<>();
        List<String> names = new ArrayList<>();
        PerfGroupEvent events = new PerfGroupEvent("cycles,instructions");
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
}
