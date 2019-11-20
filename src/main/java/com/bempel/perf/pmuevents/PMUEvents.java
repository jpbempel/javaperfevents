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
package com.bempel.perf.pmuevents;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PMUEvents {
    private static PMUEventMap eventMap;
    private static Map<String, String> cpuIdMap = new HashMap<>();
    static {
		cpuIdMap.put("GenuineIntel-6-56", "Broadwellde");
		cpuIdMap.put("GenuineIntel-6-3D", "Broadwell");
		cpuIdMap.put("GenuineIntel-6-47", "Broadwell");
		cpuIdMap.put("GenuineIntel-6-4F", "Broadwellx");
		cpuIdMap.put("GenuineIntel-6-1C", "Bonnell");
		cpuIdMap.put("GenuineIntel-6-26", "Bonnell");
		cpuIdMap.put("GenuineIntel-6-27", "Bonnell");
		cpuIdMap.put("GenuineIntel-6-36", "Bonnell");
		cpuIdMap.put("GenuineIntel-6-35", "Bonnell");
		cpuIdMap.put("GenuineIntel-6-5C", "Goldmont");
		cpuIdMap.put("GenuineIntel-6-7A", "Goldmontplus");
		cpuIdMap.put("GenuineIntel-6-3C", "Haswell");
		cpuIdMap.put("GenuineIntel-6-45", "Haswell");
		cpuIdMap.put("GenuineIntel-6-46", "Haswell");
		cpuIdMap.put("GenuineIntel-6-3F", "Haswellx");
		cpuIdMap.put("GenuineIntel-6-3A", "Ivybridge");
		cpuIdMap.put("GenuineIntel-6-3E", "Ivytown");
		cpuIdMap.put("GenuineIntel-6-2D", "Jaketown");
		cpuIdMap.put("GenuineIntel-6-57", "Knightslanding");
		cpuIdMap.put("GenuineIntel-6-85", "Knightslanding");
		cpuIdMap.put("GenuineIntel-6-1E", "Nehalemep");
		cpuIdMap.put("GenuineIntel-6-1F", "Nehalemep");
		cpuIdMap.put("GenuineIntel-6-1A", "Nehalemep");
		cpuIdMap.put("GenuineIntel-6-2E", "Nehalemex");
		cpuIdMap.put("GenuineIntel-6-[4589]E", "Skylake");
		cpuIdMap.put("GenuineIntel-6-37", "Silvermont");
		cpuIdMap.put("GenuineIntel-6-4D", "Silvermont");
		cpuIdMap.put("GenuineIntel-6-4C", "Silvermont");
		cpuIdMap.put("GenuineIntel-6-2A", "Sandybridge");
		cpuIdMap.put("GenuineIntel-6-2C", "Westmereep_dp");
		cpuIdMap.put("GenuineIntel-6-25", "Westmereep_sp");
		cpuIdMap.put("GenuineIntel-6-2F", "Westmereex");
		cpuIdMap.put("GenuineIntel-6-55", "Skylakex");
		installEventMap();
    }

    public static void register(PMUEventMap eventMap) {
		PMUEvents.eventMap = eventMap;
    }

    public static PMUEventMap getPMUEventMap() {
        return eventMap;
    }

    private static void installEventMap() {
    	String cpuId = CPUID.getCpuId();
        for (Map.Entry<String, String> entry : cpuIdMap.entrySet()) {
            if (Pattern.matches(entry.getKey(), cpuId)) {
            	try {
					Class.forName(PMUEvents.class.getPackage().getName() + "." + entry.getValue(), true, PMUEvents.class.getClassLoader());
				}
            	catch (Exception ex) {
            		ex.printStackTrace();
				}
            }
        }
    }
}