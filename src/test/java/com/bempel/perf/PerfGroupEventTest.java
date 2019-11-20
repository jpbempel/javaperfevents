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
