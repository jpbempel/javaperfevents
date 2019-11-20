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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PerfEventTest {

    @Test
    public void start_read_shutdown() {
        PerfEvent cycles = new PerfEvent("cycles");
        cycles.start();
        try {
            assertTrue(cycles.read() > 0);
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
            long previous = instructions.read();
            assertTrue(previous > 0);
            instructions.reset();
            long value = instructions.read();
            assertTrue(value > 0);
            assertTrue(previous > value);
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
            long value = instructions.read();
            assertTrue (value > 0);
            instructions.disable();
            instructions.reset();
            assertEquals(0, instructions.read());
        } finally {
            instructions.shutdown();
        }
    }

    @Test
    public void start_read_shutdown_start_read_shutdown() {
        PerfEvent cycles = new PerfEvent("cycles");
        cycles.start();
        try {
            assertTrue(cycles.read() > 0);
        } finally {
            cycles.shutdown();
        }
        cycles.start();
        try {
            assertTrue(cycles.read() > 0);
        } finally {
            cycles.shutdown();
        }
    }
}