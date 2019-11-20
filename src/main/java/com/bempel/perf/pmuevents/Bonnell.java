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

import java.util.*;
import java.util.function.Consumer;

public class Bonnell implements PMUEventMap {
    private  final Map<String, PMUEvent> eventMap = new HashMap<>();

    static {
        PMUEvents.register(new Bonnell());
    }

    {
		eventMap.put("L2_ADS.SELF", new PMUEvent(null, "0x21", "0,1", "0x40", "L2_ADS.SELF", "200000", "Cycles L2 address bus is in use.", null));
		eventMap.put("L2_DBUS_BUSY.SELF", new PMUEvent(null, "0x22", "0,1", "0x40", "L2_DBUS_BUSY.SELF", "200000", "Cycles the L2 cache data bus is busy.", null));
		eventMap.put("L2_DBUS_BUSY_RD.SELF", new PMUEvent(null, "0x23", "0,1", "0x40", "L2_DBUS_BUSY_RD.SELF", "200000", "Cycles the L2 transfers data to the core.", null));
		eventMap.put("L2_LINES_IN.SELF.ANY", new PMUEvent(null, "0x24", "0,1", "0x70", "L2_LINES_IN.SELF.ANY", "200000", "L2 cache misses.", null));
		eventMap.put("L2_LINES_IN.SELF.DEMAND", new PMUEvent(null, "0x24", "0,1", "0x40", "L2_LINES_IN.SELF.DEMAND", "200000", "L2 cache misses.", null));
		eventMap.put("L2_LINES_IN.SELF.PREFETCH", new PMUEvent(null, "0x24", "0,1", "0x50", "L2_LINES_IN.SELF.PREFETCH", "200000", "L2 cache misses.", null));
		eventMap.put("L2_M_LINES_IN.SELF", new PMUEvent(null, "0x25", "0,1", "0x40", "L2_M_LINES_IN.SELF", "200000", "L2 cache line modifications.", null));
		eventMap.put("L2_LINES_OUT.SELF.ANY", new PMUEvent(null, "0x26", "0,1", "0x70", "L2_LINES_OUT.SELF.ANY", "200000", "L2 cache lines evicted.", null));
		eventMap.put("L2_LINES_OUT.SELF.DEMAND", new PMUEvent(null, "0x26", "0,1", "0x40", "L2_LINES_OUT.SELF.DEMAND", "200000", "L2 cache lines evicted.", null));
		eventMap.put("L2_LINES_OUT.SELF.PREFETCH", new PMUEvent(null, "0x26", "0,1", "0x50", "L2_LINES_OUT.SELF.PREFETCH", "200000", "L2 cache lines evicted.", null));
		eventMap.put("L2_M_LINES_OUT.SELF.ANY", new PMUEvent(null, "0x27", "0,1", "0x70", "L2_M_LINES_OUT.SELF.ANY", "200000", "Modified lines evicted from the L2 cache", null));
		eventMap.put("L2_M_LINES_OUT.SELF.DEMAND", new PMUEvent(null, "0x27", "0,1", "0x40", "L2_M_LINES_OUT.SELF.DEMAND", "200000", "Modified lines evicted from the L2 cache", null));
		eventMap.put("L2_M_LINES_OUT.SELF.PREFETCH", new PMUEvent(null, "0x27", "0,1", "0x50", "L2_M_LINES_OUT.SELF.PREFETCH", "200000", "Modified lines evicted from the L2 cache", null));
		eventMap.put("L2_IFETCH.SELF.E_STATE", new PMUEvent(null, "0x28", "0,1", "0x44", "L2_IFETCH.SELF.E_STATE", "200000", "L2 cacheable instruction fetch requests", null));
		eventMap.put("L2_IFETCH.SELF.I_STATE", new PMUEvent(null, "0x28", "0,1", "0x41", "L2_IFETCH.SELF.I_STATE", "200000", "L2 cacheable instruction fetch requests", null));
		eventMap.put("L2_IFETCH.SELF.M_STATE", new PMUEvent(null, "0x28", "0,1", "0x48", "L2_IFETCH.SELF.M_STATE", "200000", "L2 cacheable instruction fetch requests", null));
		eventMap.put("L2_IFETCH.SELF.S_STATE", new PMUEvent(null, "0x28", "0,1", "0x42", "L2_IFETCH.SELF.S_STATE", "200000", "L2 cacheable instruction fetch requests", null));
		eventMap.put("L2_IFETCH.SELF.MESI", new PMUEvent(null, "0x28", "0,1", "0x4f", "L2_IFETCH.SELF.MESI", "200000", "L2 cacheable instruction fetch requests", null));
		eventMap.put("L2_LD.SELF.ANY.E_STATE", new PMUEvent(null, "0x29", "0,1", "0x74", "L2_LD.SELF.ANY.E_STATE", "200000", "L2 cache reads", null));
		eventMap.put("L2_LD.SELF.ANY.I_STATE", new PMUEvent(null, "0x29", "0,1", "0x71", "L2_LD.SELF.ANY.I_STATE", "200000", "L2 cache reads", null));
		eventMap.put("L2_LD.SELF.ANY.M_STATE", new PMUEvent(null, "0x29", "0,1", "0x78", "L2_LD.SELF.ANY.M_STATE", "200000", "L2 cache reads", null));
		eventMap.put("L2_LD.SELF.ANY.S_STATE", new PMUEvent(null, "0x29", "0,1", "0x72", "L2_LD.SELF.ANY.S_STATE", "200000", "L2 cache reads", null));
		eventMap.put("L2_LD.SELF.ANY.MESI", new PMUEvent(null, "0x29", "0,1", "0x7f", "L2_LD.SELF.ANY.MESI", "200000", "L2 cache reads", null));
		eventMap.put("L2_LD.SELF.DEMAND.E_STATE", new PMUEvent(null, "0x29", "0,1", "0x44", "L2_LD.SELF.DEMAND.E_STATE", "200000", "L2 cache reads", null));
		eventMap.put("L2_LD.SELF.DEMAND.I_STATE", new PMUEvent(null, "0x29", "0,1", "0x41", "L2_LD.SELF.DEMAND.I_STATE", "200000", "L2 cache reads", null));
		eventMap.put("L2_LD.SELF.DEMAND.M_STATE", new PMUEvent(null, "0x29", "0,1", "0x48", "L2_LD.SELF.DEMAND.M_STATE", "200000", "L2 cache reads", null));
		eventMap.put("L2_LD.SELF.DEMAND.S_STATE", new PMUEvent(null, "0x29", "0,1", "0x42", "L2_LD.SELF.DEMAND.S_STATE", "200000", "L2 cache reads", null));
		eventMap.put("L2_LD.SELF.DEMAND.MESI", new PMUEvent(null, "0x29", "0,1", "0x4f", "L2_LD.SELF.DEMAND.MESI", "200000", "L2 cache reads", null));
		eventMap.put("L2_LD.SELF.PREFETCH.E_STATE", new PMUEvent(null, "0x29", "0,1", "0x54", "L2_LD.SELF.PREFETCH.E_STATE", "200000", "L2 cache reads", null));
		eventMap.put("L2_LD.SELF.PREFETCH.I_STATE", new PMUEvent(null, "0x29", "0,1", "0x51", "L2_LD.SELF.PREFETCH.I_STATE", "200000", "L2 cache reads", null));
		eventMap.put("L2_LD.SELF.PREFETCH.M_STATE", new PMUEvent(null, "0x29", "0,1", "0x58", "L2_LD.SELF.PREFETCH.M_STATE", "200000", "L2 cache reads", null));
		eventMap.put("L2_LD.SELF.PREFETCH.S_STATE", new PMUEvent(null, "0x29", "0,1", "0x52", "L2_LD.SELF.PREFETCH.S_STATE", "200000", "L2 cache reads", null));
		eventMap.put("L2_LD.SELF.PREFETCH.MESI", new PMUEvent(null, "0x29", "0,1", "0x5f", "L2_LD.SELF.PREFETCH.MESI", "200000", "L2 cache reads", null));
		eventMap.put("L2_ST.SELF.E_STATE", new PMUEvent(null, "0x2A", "0,1", "0x44", "L2_ST.SELF.E_STATE", "200000", "L2 store requests", null));
		eventMap.put("L2_ST.SELF.I_STATE", new PMUEvent(null, "0x2A", "0,1", "0x41", "L2_ST.SELF.I_STATE", "200000", "L2 store requests", null));
		eventMap.put("L2_ST.SELF.M_STATE", new PMUEvent(null, "0x2A", "0,1", "0x48", "L2_ST.SELF.M_STATE", "200000", "L2 store requests", null));
		eventMap.put("L2_ST.SELF.S_STATE", new PMUEvent(null, "0x2A", "0,1", "0x42", "L2_ST.SELF.S_STATE", "200000", "L2 store requests", null));
		eventMap.put("L2_ST.SELF.MESI", new PMUEvent(null, "0x2A", "0,1", "0x4f", "L2_ST.SELF.MESI", "200000", "L2 store requests", null));
		eventMap.put("L2_LOCK.SELF.E_STATE", new PMUEvent(null, "0x2B", "0,1", "0x44", "L2_LOCK.SELF.E_STATE", "200000", "L2 locked accesses", null));
		eventMap.put("L2_LOCK.SELF.I_STATE", new PMUEvent(null, "0x2B", "0,1", "0x41", "L2_LOCK.SELF.I_STATE", "200000", "L2 locked accesses", null));
		eventMap.put("L2_LOCK.SELF.M_STATE", new PMUEvent(null, "0x2B", "0,1", "0x48", "L2_LOCK.SELF.M_STATE", "200000", "L2 locked accesses", null));
		eventMap.put("L2_LOCK.SELF.S_STATE", new PMUEvent(null, "0x2B", "0,1", "0x42", "L2_LOCK.SELF.S_STATE", "200000", "L2 locked accesses", null));
		eventMap.put("L2_LOCK.SELF.MESI", new PMUEvent(null, "0x2B", "0,1", "0x4f", "L2_LOCK.SELF.MESI", "200000", "L2 locked accesses", null));
		eventMap.put("L2_DATA_RQSTS.SELF.E_STATE", new PMUEvent(null, "0x2C", "0,1", "0x44", "L2_DATA_RQSTS.SELF.E_STATE", "200000", "All data requests from the L1 data cache", null));
		eventMap.put("L2_DATA_RQSTS.SELF.I_STATE", new PMUEvent(null, "0x2C", "0,1", "0x41", "L2_DATA_RQSTS.SELF.I_STATE", "200000", "All data requests from the L1 data cache", null));
		eventMap.put("L2_DATA_RQSTS.SELF.M_STATE", new PMUEvent(null, "0x2C", "0,1", "0x48", "L2_DATA_RQSTS.SELF.M_STATE", "200000", "All data requests from the L1 data cache", null));
		eventMap.put("L2_DATA_RQSTS.SELF.S_STATE", new PMUEvent(null, "0x2C", "0,1", "0x42", "L2_DATA_RQSTS.SELF.S_STATE", "200000", "All data requests from the L1 data cache", null));
		eventMap.put("L2_DATA_RQSTS.SELF.MESI", new PMUEvent(null, "0x2C", "0,1", "0x4f", "L2_DATA_RQSTS.SELF.MESI", "200000", "All data requests from the L1 data cache", null));
		eventMap.put("L2_LD_IFETCH.SELF.E_STATE", new PMUEvent(null, "0x2D", "0,1", "0x44", "L2_LD_IFETCH.SELF.E_STATE", "200000", "All read requests from L1 instruction and data caches", null));
		eventMap.put("L2_LD_IFETCH.SELF.I_STATE", new PMUEvent(null, "0x2D", "0,1", "0x41", "L2_LD_IFETCH.SELF.I_STATE", "200000", "All read requests from L1 instruction and data caches", null));
		eventMap.put("L2_LD_IFETCH.SELF.M_STATE", new PMUEvent(null, "0x2D", "0,1", "0x48", "L2_LD_IFETCH.SELF.M_STATE", "200000", "All read requests from L1 instruction and data caches", null));
		eventMap.put("L2_LD_IFETCH.SELF.S_STATE", new PMUEvent(null, "0x2D", "0,1", "0x42", "L2_LD_IFETCH.SELF.S_STATE", "200000", "All read requests from L1 instruction and data caches", null));
		eventMap.put("L2_LD_IFETCH.SELF.MESI", new PMUEvent(null, "0x2D", "0,1", "0x4f", "L2_LD_IFETCH.SELF.MESI", "200000", "All read requests from L1 instruction and data caches", null));
		eventMap.put("L2_RQSTS.SELF.ANY.E_STATE", new PMUEvent(null, "0x2E", "0,1", "0x74", "L2_RQSTS.SELF.ANY.E_STATE", "200000", "L2 cache requests", null));
		eventMap.put("L2_RQSTS.SELF.ANY.I_STATE", new PMUEvent(null, "0x2E", "0,1", "0x71", "L2_RQSTS.SELF.ANY.I_STATE", "200000", "L2 cache requests", null));
		eventMap.put("L2_RQSTS.SELF.ANY.M_STATE", new PMUEvent(null, "0x2E", "0,1", "0x78", "L2_RQSTS.SELF.ANY.M_STATE", "200000", "L2 cache requests", null));
		eventMap.put("L2_RQSTS.SELF.ANY.S_STATE", new PMUEvent(null, "0x2E", "0,1", "0x72", "L2_RQSTS.SELF.ANY.S_STATE", "200000", "L2 cache requests", null));
		eventMap.put("L2_RQSTS.SELF.ANY.MESI", new PMUEvent(null, "0x2E", "0,1", "0x7f", "L2_RQSTS.SELF.ANY.MESI", "200000", "L2 cache requests", null));
		eventMap.put("L2_RQSTS.SELF.DEMAND.E_STATE", new PMUEvent(null, "0x2E", "0,1", "0x44", "L2_RQSTS.SELF.DEMAND.E_STATE", "200000", "L2 cache requests", null));
		eventMap.put("L2_RQSTS.SELF.DEMAND.M_STATE", new PMUEvent(null, "0x2E", "0,1", "0x48", "L2_RQSTS.SELF.DEMAND.M_STATE", "200000", "L2 cache requests", null));
		eventMap.put("L2_RQSTS.SELF.DEMAND.S_STATE", new PMUEvent(null, "0x2E", "0,1", "0x42", "L2_RQSTS.SELF.DEMAND.S_STATE", "200000", "L2 cache requests", null));
		eventMap.put("L2_RQSTS.SELF.PREFETCH.E_STATE", new PMUEvent(null, "0x2E", "0,1", "0x54", "L2_RQSTS.SELF.PREFETCH.E_STATE", "200000", "L2 cache requests", null));
		eventMap.put("L2_RQSTS.SELF.PREFETCH.I_STATE", new PMUEvent(null, "0x2E", "0,1", "0x51", "L2_RQSTS.SELF.PREFETCH.I_STATE", "200000", "L2 cache requests", null));
		eventMap.put("L2_RQSTS.SELF.PREFETCH.M_STATE", new PMUEvent(null, "0x2E", "0,1", "0x58", "L2_RQSTS.SELF.PREFETCH.M_STATE", "200000", "L2 cache requests", null));
		eventMap.put("L2_RQSTS.SELF.PREFETCH.S_STATE", new PMUEvent(null, "0x2E", "0,1", "0x52", "L2_RQSTS.SELF.PREFETCH.S_STATE", "200000", "L2 cache requests", null));
		eventMap.put("L2_RQSTS.SELF.PREFETCH.MESI", new PMUEvent(null, "0x2E", "0,1", "0x5f", "L2_RQSTS.SELF.PREFETCH.MESI", "200000", "L2 cache requests", null));
		eventMap.put("L2_RQSTS.SELF.DEMAND.I_STATE", new PMUEvent(null, "0x2E", "0,1", "0x41", "L2_RQSTS.SELF.DEMAND.I_STATE", "200000", "L2 cache demand requests from this core that missed the L2", null));
		eventMap.put("L2_RQSTS.SELF.DEMAND.MESI", new PMUEvent(null, "0x2E", "0,1", "0x4f", "L2_RQSTS.SELF.DEMAND.MESI", "200000", "L2 cache demand requests from this core", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.ANY.E_STATE", new PMUEvent(null, "0x30", "0,1", "0x74", "L2_REJECT_BUSQ.SELF.ANY.E_STATE", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.ANY.I_STATE", new PMUEvent(null, "0x30", "0,1", "0x71", "L2_REJECT_BUSQ.SELF.ANY.I_STATE", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.ANY.M_STATE", new PMUEvent(null, "0x30", "0,1", "0x78", "L2_REJECT_BUSQ.SELF.ANY.M_STATE", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.ANY.S_STATE", new PMUEvent(null, "0x30", "0,1", "0x72", "L2_REJECT_BUSQ.SELF.ANY.S_STATE", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.ANY.MESI", new PMUEvent(null, "0x30", "0,1", "0x7f", "L2_REJECT_BUSQ.SELF.ANY.MESI", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.DEMAND.E_STATE", new PMUEvent(null, "0x30", "0,1", "0x44", "L2_REJECT_BUSQ.SELF.DEMAND.E_STATE", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.DEMAND.I_STATE", new PMUEvent(null, "0x30", "0,1", "0x41", "L2_REJECT_BUSQ.SELF.DEMAND.I_STATE", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.DEMAND.M_STATE", new PMUEvent(null, "0x30", "0,1", "0x48", "L2_REJECT_BUSQ.SELF.DEMAND.M_STATE", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.DEMAND.S_STATE", new PMUEvent(null, "0x30", "0,1", "0x42", "L2_REJECT_BUSQ.SELF.DEMAND.S_STATE", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.DEMAND.MESI", new PMUEvent(null, "0x30", "0,1", "0x4f", "L2_REJECT_BUSQ.SELF.DEMAND.MESI", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.PREFETCH.E_STATE", new PMUEvent(null, "0x30", "0,1", "0x54", "L2_REJECT_BUSQ.SELF.PREFETCH.E_STATE", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.PREFETCH.I_STATE", new PMUEvent(null, "0x30", "0,1", "0x51", "L2_REJECT_BUSQ.SELF.PREFETCH.I_STATE", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.PREFETCH.M_STATE", new PMUEvent(null, "0x30", "0,1", "0x58", "L2_REJECT_BUSQ.SELF.PREFETCH.M_STATE", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.PREFETCH.S_STATE", new PMUEvent(null, "0x30", "0,1", "0x52", "L2_REJECT_BUSQ.SELF.PREFETCH.S_STATE", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_REJECT_BUSQ.SELF.PREFETCH.MESI", new PMUEvent(null, "0x30", "0,1", "0x5f", "L2_REJECT_BUSQ.SELF.PREFETCH.MESI", "200000", "Rejected L2 cache requests", null));
		eventMap.put("L2_NO_REQ.SELF", new PMUEvent(null, "0x32", "0,1", "0x40", "L2_NO_REQ.SELF", "200000", "Cycles no L2 cache requests are pending", null));
		eventMap.put("L1D_CACHE.LD", new PMUEvent(null, "0x40", "0,1", "0xa1", "L1D_CACHE.LD", "2000000", "L1 Cacheable Data Reads", null));
		eventMap.put("L1D_CACHE.ST", new PMUEvent(null, "0x40", "0,1", "0xa2", "L1D_CACHE.ST", "2000000", "L1 Cacheable Data Writes", null));
		eventMap.put("L1D_CACHE.ALL_REF", new PMUEvent(null, "0x40", "0,1", "0x83", "L1D_CACHE.ALL_REF", "2000000", "L1 Data reads and writes", null));
		eventMap.put("L1D_CACHE.ALL_CACHE_REF", new PMUEvent(null, "0x40", "0,1", "0xa3", "L1D_CACHE.ALL_CACHE_REF", "2000000", "L1 Data Cacheable reads and writes", null));
		eventMap.put("L1D_CACHE.REPL", new PMUEvent(null, "0x40", "0,1", "0x8", "L1D_CACHE.REPL", "200000", "L1 Data line replacements", null));
		eventMap.put("L1D_CACHE.REPLM", new PMUEvent(null, "0x40", "0,1", "0x48", "L1D_CACHE.REPLM", "200000", "Modified cache lines allocated in the L1 data cache", null));
		eventMap.put("L1D_CACHE.EVICT", new PMUEvent(null, "0x40", "0,1", "0x10", "L1D_CACHE.EVICT", "200000", "Modified cache lines evicted from the L1 data cache", null));
		eventMap.put("MEM_LOAD_RETIRED.L2_HIT", new PMUEvent(null, "0xCB", "0,1", "0x1", "MEM_LOAD_RETIRED.L2_HIT", "200000", "Retired loads that hit the L2 cache (precise event).", null));
		eventMap.put("MEM_LOAD_RETIRED.L2_MISS", new PMUEvent(null, "0xCB", "0,1", "0x2", "MEM_LOAD_RETIRED.L2_MISS", "10000", "Retired loads that miss the L2 cache", null));
		eventMap.put("X87_COMP_OPS_EXE.ANY.S", new PMUEvent(null, "0x10", "0,1", "0x1", "X87_COMP_OPS_EXE.ANY.S", "2000000", "Floating point computational micro-ops executed.", null));
		eventMap.put("X87_COMP_OPS_EXE.ANY.AR", new PMUEvent(null, "0x10", "0,1", "0x81", "X87_COMP_OPS_EXE.ANY.AR", "2000000", "Floating point computational micro-ops retired.", null));
		eventMap.put("X87_COMP_OPS_EXE.FXCH.S", new PMUEvent(null, "0x10", "0,1", "0x2", "X87_COMP_OPS_EXE.FXCH.S", "2000000", "FXCH uops executed.", null));
		eventMap.put("X87_COMP_OPS_EXE.FXCH.AR", new PMUEvent(null, "0x10", "0,1", "0x82", "X87_COMP_OPS_EXE.FXCH.AR", "2000000", "FXCH uops retired.", null));
		eventMap.put("FP_ASSIST.S", new PMUEvent(null, "0x11", "0,1", "0x1", "FP_ASSIST.S", "10000", "Floating point assists.", null));
		eventMap.put("FP_ASSIST.AR", new PMUEvent(null, "0x11", "0,1", "0x81", "FP_ASSIST.AR", "10000", "Floating point assists for retired operations.", null));
		eventMap.put("SIMD_UOPS_EXEC.S", new PMUEvent(null, "0xB0", "0,1", "0x0", "SIMD_UOPS_EXEC.S", "2000000", "SIMD micro-ops executed (excluding stores).", null));
		eventMap.put("SIMD_UOPS_EXEC.AR", new PMUEvent(null, "0xB0", "0,1", "0x80", "SIMD_UOPS_EXEC.AR", "2000000", "SIMD micro-ops retired (excluding stores).", null));
		eventMap.put("SIMD_SAT_UOP_EXEC.S", new PMUEvent(null, "0xB1", "0,1", "0x0", "SIMD_SAT_UOP_EXEC.S", "2000000", "SIMD saturated arithmetic micro-ops executed.", null));
		eventMap.put("SIMD_SAT_UOP_EXEC.AR", new PMUEvent(null, "0xB1", "0,1", "0x80", "SIMD_SAT_UOP_EXEC.AR", "2000000", "SIMD saturated arithmetic micro-ops retired.", null));
		eventMap.put("SIMD_UOP_TYPE_EXEC.MUL.S", new PMUEvent(null, "0xB3", "0,1", "0x1", "SIMD_UOP_TYPE_EXEC.MUL.S", "2000000", "SIMD packed multiply micro-ops executed", null));
		eventMap.put("SIMD_UOP_TYPE_EXEC.MUL.AR", new PMUEvent(null, "0xB3", "0,1", "0x81", "SIMD_UOP_TYPE_EXEC.MUL.AR", "2000000", "SIMD packed multiply micro-ops retired", null));
		eventMap.put("SIMD_UOP_TYPE_EXEC.SHIFT.S", new PMUEvent(null, "0xB3", "0,1", "0x2", "SIMD_UOP_TYPE_EXEC.SHIFT.S", "2000000", "SIMD packed shift micro-ops executed", null));
		eventMap.put("SIMD_UOP_TYPE_EXEC.SHIFT.AR", new PMUEvent(null, "0xB3", "0,1", "0x82", "SIMD_UOP_TYPE_EXEC.SHIFT.AR", "2000000", "SIMD packed shift micro-ops retired", null));
		eventMap.put("SIMD_UOP_TYPE_EXEC.PACK.S", new PMUEvent(null, "0xB3", "0,1", "0x4", "SIMD_UOP_TYPE_EXEC.PACK.S", "2000000", "SIMD packed micro-ops executed", null));
		eventMap.put("SIMD_UOP_TYPE_EXEC.PACK.AR", new PMUEvent(null, "0xB3", "0,1", "0x84", "SIMD_UOP_TYPE_EXEC.PACK.AR", "2000000", "SIMD packed micro-ops retired", null));
		eventMap.put("SIMD_UOP_TYPE_EXEC.UNPACK.S", new PMUEvent(null, "0xB3", "0,1", "0x8", "SIMD_UOP_TYPE_EXEC.UNPACK.S", "2000000", "SIMD unpacked micro-ops executed", null));
		eventMap.put("SIMD_UOP_TYPE_EXEC.UNPACK.AR", new PMUEvent(null, "0xB3", "0,1", "0x88", "SIMD_UOP_TYPE_EXEC.UNPACK.AR", "2000000", "SIMD unpacked micro-ops retired", null));
		eventMap.put("SIMD_UOP_TYPE_EXEC.LOGICAL.S", new PMUEvent(null, "0xB3", "0,1", "0x10", "SIMD_UOP_TYPE_EXEC.LOGICAL.S", "2000000", "SIMD packed logical micro-ops executed", null));
		eventMap.put("SIMD_UOP_TYPE_EXEC.LOGICAL.AR", new PMUEvent(null, "0xB3", "0,1", "0x90", "SIMD_UOP_TYPE_EXEC.LOGICAL.AR", "2000000", "SIMD packed logical micro-ops retired", null));
		eventMap.put("SIMD_UOP_TYPE_EXEC.ARITHMETIC.S", new PMUEvent(null, "0xB3", "0,1", "0x20", "SIMD_UOP_TYPE_EXEC.ARITHMETIC.S", "2000000", "SIMD packed arithmetic micro-ops executed", null));
		eventMap.put("SIMD_UOP_TYPE_EXEC.ARITHMETIC.AR", new PMUEvent(null, "0xB3", "0,1", "0xa0", "SIMD_UOP_TYPE_EXEC.ARITHMETIC.AR", "2000000", "SIMD packed arithmetic micro-ops retired", null));
		eventMap.put("SIMD_INST_RETIRED.PACKED_SINGLE", new PMUEvent(null, "0xC7", "0,1", "0x1", "SIMD_INST_RETIRED.PACKED_SINGLE", "2000000", "Retired Streaming SIMD Extensions (SSE) packed-single instructions.", null));
		eventMap.put("SIMD_INST_RETIRED.SCALAR_SINGLE", new PMUEvent(null, "0xC7", "0,1", "0x2", "SIMD_INST_RETIRED.SCALAR_SINGLE", "2000000", "Retired Streaming SIMD Extensions (SSE) scalar-single instructions.", null));
		eventMap.put("SIMD_INST_RETIRED.SCALAR_DOUBLE", new PMUEvent(null, "0xC7", "0,1", "0x8", "SIMD_INST_RETIRED.SCALAR_DOUBLE", "2000000", "Retired Streaming SIMD Extensions 2 (SSE2) scalar-double instructions.", null));
		eventMap.put("SIMD_INST_RETIRED.VECTOR", new PMUEvent(null, "0xC7", "0,1", "0x10", "SIMD_INST_RETIRED.VECTOR", "2000000", "Retired Streaming SIMD Extensions 2 (SSE2) vector instructions.", null));
		eventMap.put("SIMD_COMP_INST_RETIRED.PACKED_SINGLE", new PMUEvent(null, "0xCA", "0,1", "0x1", "SIMD_COMP_INST_RETIRED.PACKED_SINGLE", "2000000", "Retired computational Streaming SIMD Extensions (SSE) packed-single instructions.", null));
		eventMap.put("SIMD_COMP_INST_RETIRED.SCALAR_SINGLE", new PMUEvent(null, "0xCA", "0,1", "0x2", "SIMD_COMP_INST_RETIRED.SCALAR_SINGLE", "2000000", "Retired computational Streaming SIMD Extensions (SSE) scalar-single instructions.", null));
		eventMap.put("SIMD_COMP_INST_RETIRED.SCALAR_DOUBLE", new PMUEvent(null, "0xCA", "0,1", "0x8", "SIMD_COMP_INST_RETIRED.SCALAR_DOUBLE", "2000000", "Retired computational Streaming SIMD Extensions 2 (SSE2) scalar-double instructions.", null));
		eventMap.put("SIMD_ASSIST", new PMUEvent(null, "0xCD", "0,1", "0x0", "SIMD_ASSIST", "100000", "SIMD assists invoked.", null));
		eventMap.put("SIMD_INSTR_RETIRED", new PMUEvent(null, "0xCE", "0,1", "0x0", "SIMD_INSTR_RETIRED", "2000000", "SIMD Instructions retired.", null));
		eventMap.put("SIMD_SAT_INSTR_RETIRED", new PMUEvent(null, "0xCF", "0,1", "0x0", "SIMD_SAT_INSTR_RETIRED", "2000000", "Saturated arithmetic instructions retired.", null));
		eventMap.put("ICACHE.ACCESSES", new PMUEvent(null, "0x80", "0,1", "0x3", "ICACHE.ACCESSES", "200000", "Instruction fetches.", null));
		eventMap.put("ICACHE.HIT", new PMUEvent(null, "0x80", "0,1", "0x1", "ICACHE.HIT", "200000", "Icache hit", null));
		eventMap.put("ICACHE.MISSES", new PMUEvent(null, "0x80", "0,1", "0x2", "ICACHE.MISSES", "200000", "Icache miss", null));
		eventMap.put("CYCLES_ICACHE_MEM_STALLED.ICACHE_MEM_STALLED", new PMUEvent(null, "0x86", "0,1", "0x1", "CYCLES_ICACHE_MEM_STALLED.ICACHE_MEM_STALLED", "2000000", "Cycles during which instruction fetches are  stalled.", null));
		eventMap.put("DECODE_STALL.PFB_EMPTY", new PMUEvent(null, "0x87", "0,1", "0x1", "DECODE_STALL.PFB_EMPTY", "2000000", "Decode stall due to PFB empty", null));
		eventMap.put("DECODE_STALL.IQ_FULL", new PMUEvent(null, "0x87", "0,1", "0x2", "DECODE_STALL.IQ_FULL", "2000000", "Decode stall due to IQ full", null));
		eventMap.put("MACRO_INSTS.NON_CISC_DECODED", new PMUEvent(null, "0xAA", "0,1", "0x1", "MACRO_INSTS.NON_CISC_DECODED", "2000000", "Non-CISC nacro instructions decoded", null));
		eventMap.put("MACRO_INSTS.CISC_DECODED", new PMUEvent(null, "0xAA", "0,1", "0x2", "MACRO_INSTS.CISC_DECODED", "2000000", "CISC macro instructions decoded", null));
		eventMap.put("MACRO_INSTS.ALL_DECODED", new PMUEvent(null, "0xAA", "0,1", "0x3", "MACRO_INSTS.ALL_DECODED", "2000000", "All Instructions decoded", null));
		eventMap.put("UOPS.MS_CYCLES", new PMUEvent(null, "0xA9", "0,1", "0x1", "UOPS.MS_CYCLES", "2000000", "This event counts the cycles where 1 or more uops are issued by the micro-sequencer (MS), including microcode assists and inserted flows, and written to the IQ. ", null));
		eventMap.put("MISALIGN_MEM_REF.SPLIT", new PMUEvent(null, "0x5", "0,1", "0xf", "MISALIGN_MEM_REF.SPLIT", "200000", "Memory references that cross an 8-byte boundary.", null));
		eventMap.put("MISALIGN_MEM_REF.LD_SPLIT", new PMUEvent(null, "0x5", "0,1", "0x9", "MISALIGN_MEM_REF.LD_SPLIT", "200000", "Load splits", null));
		eventMap.put("MISALIGN_MEM_REF.ST_SPLIT", new PMUEvent(null, "0x5", "0,1", "0xa", "MISALIGN_MEM_REF.ST_SPLIT", "200000", "Store splits", null));
		eventMap.put("MISALIGN_MEM_REF.SPLIT.AR", new PMUEvent(null, "0x5", "0,1", "0x8f", "MISALIGN_MEM_REF.SPLIT.AR", "200000", "Memory references that cross an 8-byte boundary (At Retirement)", null));
		eventMap.put("MISALIGN_MEM_REF.LD_SPLIT.AR", new PMUEvent(null, "0x5", "0,1", "0x89", "MISALIGN_MEM_REF.LD_SPLIT.AR", "200000", "Load splits (At Retirement)", null));
		eventMap.put("MISALIGN_MEM_REF.ST_SPLIT.AR", new PMUEvent(null, "0x5", "0,1", "0x8a", "MISALIGN_MEM_REF.ST_SPLIT.AR", "200000", "Store splits (Ar Retirement)", null));
		eventMap.put("MISALIGN_MEM_REF.RMW_SPLIT", new PMUEvent(null, "0x5", "0,1", "0x8c", "MISALIGN_MEM_REF.RMW_SPLIT", "200000", "ld-op-st splits", null));
		eventMap.put("MISALIGN_MEM_REF.BUBBLE", new PMUEvent(null, "0x5", "0,1", "0x97", "MISALIGN_MEM_REF.BUBBLE", "200000", "Nonzero segbase 1 bubble", null));
		eventMap.put("MISALIGN_MEM_REF.LD_BUBBLE", new PMUEvent(null, "0x5", "0,1", "0x91", "MISALIGN_MEM_REF.LD_BUBBLE", "200000", "Nonzero segbase load 1 bubble", null));
		eventMap.put("MISALIGN_MEM_REF.ST_BUBBLE", new PMUEvent(null, "0x5", "0,1", "0x92", "MISALIGN_MEM_REF.ST_BUBBLE", "200000", "Nonzero segbase store 1 bubble", null));
		eventMap.put("MISALIGN_MEM_REF.RMW_BUBBLE", new PMUEvent(null, "0x5", "0,1", "0x94", "MISALIGN_MEM_REF.RMW_BUBBLE", "200000", "Nonzero segbase ld-op-st 1 bubble", null));
		eventMap.put("PREFETCH.PREFETCHT0", new PMUEvent(null, "0x7", "0,1", "0x81", "PREFETCH.PREFETCHT0", "200000", "Streaming SIMD Extensions (SSE) PrefetchT0 instructions executed.", null));
		eventMap.put("PREFETCH.PREFETCHT1", new PMUEvent(null, "0x7", "0,1", "0x82", "PREFETCH.PREFETCHT1", "200000", "Streaming SIMD Extensions (SSE) PrefetchT1 instructions executed.", null));
		eventMap.put("PREFETCH.PREFETCHT2", new PMUEvent(null, "0x7", "0,1", "0x84", "PREFETCH.PREFETCHT2", "200000", "Streaming SIMD Extensions (SSE) PrefetchT2 instructions executed.", null));
		eventMap.put("PREFETCH.SW_L2", new PMUEvent(null, "0x7", "0,1", "0x86", "PREFETCH.SW_L2", "200000", "Streaming SIMD Extensions (SSE) PrefetchT1 and PrefetchT2 instructions executed", null));
		eventMap.put("PREFETCH.PREFETCHNTA", new PMUEvent(null, "0x7", "0,1", "0x88", "PREFETCH.PREFETCHNTA", "200000", "Streaming SIMD Extensions (SSE) Prefetch NTA instructions executed", null));
		eventMap.put("PREFETCH.HW_PREFETCH", new PMUEvent(null, "0x7", "0,1", "0x10", "PREFETCH.HW_PREFETCH", "2000000", "L1 hardware prefetch request", null));
		eventMap.put("PREFETCH.SOFTWARE_PREFETCH", new PMUEvent(null, "0x7", "0,1", "0xf", "PREFETCH.SOFTWARE_PREFETCH", "200000", "Any Software prefetch", null));
		eventMap.put("PREFETCH.SOFTWARE_PREFETCH.AR", new PMUEvent(null, "0x7", "0,1", "0x8f", "PREFETCH.SOFTWARE_PREFETCH.AR", "200000", "Any Software prefetch", null));
		eventMap.put("SEGMENT_REG_LOADS.ANY", new PMUEvent(null, "0x6", "0,1", "0x80", "SEGMENT_REG_LOADS.ANY", "200000", "Number of segment register loads.", null));
		eventMap.put("DISPATCH_BLOCKED.ANY", new PMUEvent(null, "0x9", "0,1", "0x20", "DISPATCH_BLOCKED.ANY", "200000", "Memory cluster signals to block micro-op dispatch for any reason", null));
		eventMap.put("EIST_TRANS", new PMUEvent(null, "0x3A", "0,1", "0x0", "EIST_TRANS", "200000", "Number of Enhanced Intel SpeedStep(R) Technology (EIST) transitions", null));
		eventMap.put("THERMAL_TRIP", new PMUEvent(null, "0x3B", "0,1", "0xc0", "THERMAL_TRIP", "200000", "Number of thermal trips", null));
		eventMap.put("BUS_REQUEST_OUTSTANDING.ALL_AGENTS", new PMUEvent(null, "0x60", "0,1", "0xe0", "BUS_REQUEST_OUTSTANDING.ALL_AGENTS", "200000", "Outstanding cacheable data read bus requests duration.", null));
		eventMap.put("BUS_REQUEST_OUTSTANDING.SELF", new PMUEvent(null, "0x60", "0,1", "0x40", "BUS_REQUEST_OUTSTANDING.SELF", "200000", "Outstanding cacheable data read bus requests duration.", null));
		eventMap.put("BUS_BNR_DRV.ALL_AGENTS", new PMUEvent(null, "0x61", "0,1", "0x20", "BUS_BNR_DRV.ALL_AGENTS", "200000", "Number of Bus Not Ready signals asserted.", null));
		eventMap.put("BUS_BNR_DRV.THIS_AGENT", new PMUEvent(null, "0x61", "0,1", "0x0", "BUS_BNR_DRV.THIS_AGENT", "200000", "Number of Bus Not Ready signals asserted.", null));
		eventMap.put("BUS_DRDY_CLOCKS.ALL_AGENTS", new PMUEvent(null, "0x62", "0,1", "0x20", "BUS_DRDY_CLOCKS.ALL_AGENTS", "200000", "Bus cycles when data is sent on the bus.", null));
		eventMap.put("BUS_DRDY_CLOCKS.THIS_AGENT", new PMUEvent(null, "0x62", "0,1", "0x0", "BUS_DRDY_CLOCKS.THIS_AGENT", "200000", "Bus cycles when data is sent on the bus.", null));
		eventMap.put("BUS_LOCK_CLOCKS.ALL_AGENTS", new PMUEvent(null, "0x63", "0,1", "0xe0", "BUS_LOCK_CLOCKS.ALL_AGENTS", "200000", "Bus cycles when a LOCK signal is asserted.", null));
		eventMap.put("BUS_LOCK_CLOCKS.SELF", new PMUEvent(null, "0x63", "0,1", "0x40", "BUS_LOCK_CLOCKS.SELF", "200000", "Bus cycles when a LOCK signal is asserted.", null));
		eventMap.put("BUS_DATA_RCV.SELF", new PMUEvent(null, "0x64", "0,1", "0x40", "BUS_DATA_RCV.SELF", "200000", "Bus cycles while processor receives data.", null));
		eventMap.put("BUS_TRANS_BRD.ALL_AGENTS", new PMUEvent(null, "0x65", "0,1", "0xe0", "BUS_TRANS_BRD.ALL_AGENTS", "200000", "Burst read bus transactions.", null));
		eventMap.put("BUS_TRANS_BRD.SELF", new PMUEvent(null, "0x65", "0,1", "0x40", "BUS_TRANS_BRD.SELF", "200000", "Burst read bus transactions.", null));
		eventMap.put("BUS_TRANS_RFO.ALL_AGENTS", new PMUEvent(null, "0x66", "0,1", "0xe0", "BUS_TRANS_RFO.ALL_AGENTS", "200000", "RFO bus transactions.", null));
		eventMap.put("BUS_TRANS_RFO.SELF", new PMUEvent(null, "0x66", "0,1", "0x40", "BUS_TRANS_RFO.SELF", "200000", "RFO bus transactions.", null));
		eventMap.put("BUS_TRANS_WB.ALL_AGENTS", new PMUEvent(null, "0x67", "0,1", "0xe0", "BUS_TRANS_WB.ALL_AGENTS", "200000", "Explicit writeback bus transactions.", null));
		eventMap.put("BUS_TRANS_WB.SELF", new PMUEvent(null, "0x67", "0,1", "0x40", "BUS_TRANS_WB.SELF", "200000", "Explicit writeback bus transactions.", null));
		eventMap.put("BUS_TRANS_IFETCH.ALL_AGENTS", new PMUEvent(null, "0x68", "0,1", "0xe0", "BUS_TRANS_IFETCH.ALL_AGENTS", "200000", "Instruction-fetch bus transactions.", null));
		eventMap.put("BUS_TRANS_IFETCH.SELF", new PMUEvent(null, "0x68", "0,1", "0x40", "BUS_TRANS_IFETCH.SELF", "200000", "Instruction-fetch bus transactions.", null));
		eventMap.put("BUS_TRANS_INVAL.ALL_AGENTS", new PMUEvent(null, "0x69", "0,1", "0xe0", "BUS_TRANS_INVAL.ALL_AGENTS", "200000", "Invalidate bus transactions.", null));
		eventMap.put("BUS_TRANS_INVAL.SELF", new PMUEvent(null, "0x69", "0,1", "0x40", "BUS_TRANS_INVAL.SELF", "200000", "Invalidate bus transactions.", null));
		eventMap.put("BUS_TRANS_PWR.ALL_AGENTS", new PMUEvent(null, "0x6A", "0,1", "0xe0", "BUS_TRANS_PWR.ALL_AGENTS", "200000", "Partial write bus transaction.", null));
		eventMap.put("BUS_TRANS_PWR.SELF", new PMUEvent(null, "0x6A", "0,1", "0x40", "BUS_TRANS_PWR.SELF", "200000", "Partial write bus transaction.", null));
		eventMap.put("BUS_TRANS_P.ALL_AGENTS", new PMUEvent(null, "0x6B", "0,1", "0xe0", "BUS_TRANS_P.ALL_AGENTS", "200000", "Partial bus transactions.", null));
		eventMap.put("BUS_TRANS_P.SELF", new PMUEvent(null, "0x6B", "0,1", "0x40", "BUS_TRANS_P.SELF", "200000", "Partial bus transactions.", null));
		eventMap.put("BUS_TRANS_IO.ALL_AGENTS", new PMUEvent(null, "0x6C", "0,1", "0xe0", "BUS_TRANS_IO.ALL_AGENTS", "200000", "IO bus transactions.", null));
		eventMap.put("BUS_TRANS_IO.SELF", new PMUEvent(null, "0x6C", "0,1", "0x40", "BUS_TRANS_IO.SELF", "200000", "IO bus transactions.", null));
		eventMap.put("BUS_TRANS_DEF.ALL_AGENTS", new PMUEvent(null, "0x6D", "0,1", "0xe0", "BUS_TRANS_DEF.ALL_AGENTS", "200000", "Deferred bus transactions.", null));
		eventMap.put("BUS_TRANS_DEF.SELF", new PMUEvent(null, "0x6D", "0,1", "0x40", "BUS_TRANS_DEF.SELF", "200000", "Deferred bus transactions.", null));
		eventMap.put("BUS_TRANS_BURST.ALL_AGENTS", new PMUEvent(null, "0x6E", "0,1", "0xe0", "BUS_TRANS_BURST.ALL_AGENTS", "200000", "Burst (full cache-line) bus transactions.", null));
		eventMap.put("BUS_TRANS_BURST.SELF", new PMUEvent(null, "0x6E", "0,1", "0x40", "BUS_TRANS_BURST.SELF", "200000", "Burst (full cache-line) bus transactions.", null));
		eventMap.put("BUS_TRANS_MEM.ALL_AGENTS", new PMUEvent(null, "0x6F", "0,1", "0xe0", "BUS_TRANS_MEM.ALL_AGENTS", "200000", "Memory bus transactions.", null));
		eventMap.put("BUS_TRANS_MEM.SELF", new PMUEvent(null, "0x6F", "0,1", "0x40", "BUS_TRANS_MEM.SELF", "200000", "Memory bus transactions.", null));
		eventMap.put("BUS_TRANS_ANY.ALL_AGENTS", new PMUEvent(null, "0x70", "0,1", "0xe0", "BUS_TRANS_ANY.ALL_AGENTS", "200000", "All bus transactions.", null));
		eventMap.put("BUS_TRANS_ANY.SELF", new PMUEvent(null, "0x70", "0,1", "0x40", "BUS_TRANS_ANY.SELF", "200000", "All bus transactions.", null));
		eventMap.put("EXT_SNOOP.THIS_AGENT.ANY", new PMUEvent(null, "0x77", "0,1", "0xb", "EXT_SNOOP.THIS_AGENT.ANY", "200000", "External snoops.", null));
		eventMap.put("EXT_SNOOP.THIS_AGENT.CLEAN", new PMUEvent(null, "0x77", "0,1", "0x1", "EXT_SNOOP.THIS_AGENT.CLEAN", "200000", "External snoops.", null));
		eventMap.put("EXT_SNOOP.THIS_AGENT.HIT", new PMUEvent(null, "0x77", "0,1", "0x2", "EXT_SNOOP.THIS_AGENT.HIT", "200000", "External snoops.", null));
		eventMap.put("EXT_SNOOP.THIS_AGENT.HITM", new PMUEvent(null, "0x77", "0,1", "0x8", "EXT_SNOOP.THIS_AGENT.HITM", "200000", "External snoops.", null));
		eventMap.put("EXT_SNOOP.ALL_AGENTS.ANY", new PMUEvent(null, "0x77", "0,1", "0x2b", "EXT_SNOOP.ALL_AGENTS.ANY", "200000", "External snoops.", null));
		eventMap.put("EXT_SNOOP.ALL_AGENTS.CLEAN", new PMUEvent(null, "0x77", "0,1", "0x21", "EXT_SNOOP.ALL_AGENTS.CLEAN", "200000", "External snoops.", null));
		eventMap.put("EXT_SNOOP.ALL_AGENTS.HIT", new PMUEvent(null, "0x77", "0,1", "0x22", "EXT_SNOOP.ALL_AGENTS.HIT", "200000", "External snoops.", null));
		eventMap.put("EXT_SNOOP.ALL_AGENTS.HITM", new PMUEvent(null, "0x77", "0,1", "0x28", "EXT_SNOOP.ALL_AGENTS.HITM", "200000", "External snoops.", null));
		eventMap.put("BUS_HIT_DRV.ALL_AGENTS", new PMUEvent(null, "0x7A", "0,1", "0x20", "BUS_HIT_DRV.ALL_AGENTS", "200000", "HIT signal asserted.", null));
		eventMap.put("BUS_HIT_DRV.THIS_AGENT", new PMUEvent(null, "0x7A", "0,1", "0x0", "BUS_HIT_DRV.THIS_AGENT", "200000", "HIT signal asserted.", null));
		eventMap.put("BUS_HITM_DRV.ALL_AGENTS", new PMUEvent(null, "0x7B", "0,1", "0x20", "BUS_HITM_DRV.ALL_AGENTS", "200000", "HITM signal asserted.", null));
		eventMap.put("BUS_HITM_DRV.THIS_AGENT", new PMUEvent(null, "0x7B", "0,1", "0x0", "BUS_HITM_DRV.THIS_AGENT", "200000", "HITM signal asserted.", null));
		eventMap.put("BUSQ_EMPTY.SELF", new PMUEvent(null, "0x7D", "0,1", "0x40", "BUSQ_EMPTY.SELF", "200000", "Bus queue is empty.", null));
		eventMap.put("SNOOP_STALL_DRV.ALL_AGENTS", new PMUEvent(null, "0x7E", "0,1", "0xe0", "SNOOP_STALL_DRV.ALL_AGENTS", "200000", "Bus stalled for snoops.", null));
		eventMap.put("SNOOP_STALL_DRV.SELF", new PMUEvent(null, "0x7E", "0,1", "0x40", "SNOOP_STALL_DRV.SELF", "200000", "Bus stalled for snoops.", null));
		eventMap.put("BUS_IO_WAIT.SELF", new PMUEvent(null, "0x7F", "0,1", "0x40", "BUS_IO_WAIT.SELF", "200000", "IO requests waiting in the bus queue.", null));
		eventMap.put("CYCLES_INT_MASKED.CYCLES_INT_MASKED", new PMUEvent(null, "0xC6", "0,1", "0x1", "CYCLES_INT_MASKED.CYCLES_INT_MASKED", "2000000", "Cycles during which interrupts are disabled.", null));
		eventMap.put("CYCLES_INT_MASKED.CYCLES_INT_PENDING_AND_MASKED", new PMUEvent(null, "0xC6", "0,1", "0x2", "CYCLES_INT_MASKED.CYCLES_INT_PENDING_AND_MASKED", "2000000", "Cycles during which interrupts are pending and disabled.", null));
		eventMap.put("HW_INT_RCV", new PMUEvent(null, "0xC8", "0,1", "0x0", "HW_INT_RCV", "200000", "Hardware interrupts received.", null));
		eventMap.put("STORE_FORWARDS.ANY", new PMUEvent(null, "0x2", "0,1", "0x83", "STORE_FORWARDS.ANY", "200000", "All store forwards", null));
		eventMap.put("STORE_FORWARDS.GOOD", new PMUEvent(null, "0x2", "0,1", "0x81", "STORE_FORWARDS.GOOD", "200000", "Good store forwards", null));
		eventMap.put("REISSUE.ANY", new PMUEvent(null, "0x3", "0,1", "0x7f", "REISSUE.ANY", "200000", "Micro-op reissues for any cause", null));
		eventMap.put("REISSUE.ANY.AR", new PMUEvent(null, "0x3", "0,1", "0xff", "REISSUE.ANY.AR", "200000", "Micro-op reissues for any cause (At Retirement)", null));
		eventMap.put("MUL.S", new PMUEvent(null, "0x12", "0,1", "0x1", "MUL.S", "2000000", "Multiply operations executed.", null));
		eventMap.put("MUL.AR", new PMUEvent(null, "0x12", "0,1", "0x81", "MUL.AR", "2000000", "Multiply operations retired", null));
		eventMap.put("DIV.S", new PMUEvent(null, "0x13", "0,1", "0x1", "DIV.S", "2000000", "Divide operations executed.", null));
		eventMap.put("DIV.AR", new PMUEvent(null, "0x13", "0,1", "0x81", "DIV.AR", "2000000", "Divide operations retired", null));
		eventMap.put("CYCLES_DIV_BUSY", new PMUEvent(null, "0x14", "0,1", "0x1", "CYCLES_DIV_BUSY", "2000000", "Cycles the divider is busy.", null));
		eventMap.put("CPU_CLK_UNHALTED.CORE_P", new PMUEvent(null, "0x3C", "0,1", "0x0", "CPU_CLK_UNHALTED.CORE_P", "2000000", "Core cycles when core is not halted", null));
		eventMap.put("CPU_CLK_UNHALTED.BUS", new PMUEvent(null, "0x3C", "0,1", "0x1", "CPU_CLK_UNHALTED.BUS", "200000", "Bus cycles when core is not halted", null));
		eventMap.put("CPU_CLK_UNHALTED.CORE", new PMUEvent(null, "0xA", "Fixed counter 2", "0x0", "CPU_CLK_UNHALTED.CORE", "2000000", "Core cycles when core is not halted", null));
		eventMap.put("CPU_CLK_UNHALTED.REF", new PMUEvent(null, "0xA", "Fixed counter 3", "0x0", "CPU_CLK_UNHALTED.REF", "2000000", "Reference cycles when core is not halted.", null));
		eventMap.put("BR_INST_TYPE_RETIRED.COND", new PMUEvent(null, "0x88", "0,1", "0x1", "BR_INST_TYPE_RETIRED.COND", "2000000", "All macro conditional branch instructions.", null));
		eventMap.put("BR_INST_TYPE_RETIRED.UNCOND", new PMUEvent(null, "0x88", "0,1", "0x2", "BR_INST_TYPE_RETIRED.UNCOND", "2000000", "All macro unconditional branch instructions, excluding calls and indirects", null));
		eventMap.put("BR_INST_TYPE_RETIRED.IND", new PMUEvent(null, "0x88", "0,1", "0x4", "BR_INST_TYPE_RETIRED.IND", "2000000", "All indirect branches that are not calls.", null));
		eventMap.put("BR_INST_TYPE_RETIRED.RET", new PMUEvent(null, "0x88", "0,1", "0x8", "BR_INST_TYPE_RETIRED.RET", "2000000", "All indirect branches that have a return mnemonic", null));
		eventMap.put("BR_INST_TYPE_RETIRED.DIR_CALL", new PMUEvent(null, "0x88", "0,1", "0x10", "BR_INST_TYPE_RETIRED.DIR_CALL", "2000000", "All non-indirect calls", null));
		eventMap.put("BR_INST_TYPE_RETIRED.IND_CALL", new PMUEvent(null, "0x88", "0,1", "0x20", "BR_INST_TYPE_RETIRED.IND_CALL", "2000000", "All indirect calls, including both register and memory indirect.", null));
		eventMap.put("BR_INST_TYPE_RETIRED.COND_TAKEN", new PMUEvent(null, "0x88", "0,1", "0x41", "BR_INST_TYPE_RETIRED.COND_TAKEN", "2000000", "Only taken macro conditional branch instructions", null));
		eventMap.put("BR_MISSP_TYPE_RETIRED.COND", new PMUEvent(null, "0x89", "0,1", "0x1", "BR_MISSP_TYPE_RETIRED.COND", "200000", "Mispredicted cond branch instructions retired", null));
		eventMap.put("BR_MISSP_TYPE_RETIRED.IND", new PMUEvent(null, "0x89", "0,1", "0x2", "BR_MISSP_TYPE_RETIRED.IND", "200000", "Mispredicted ind branches that are not calls", null));
		eventMap.put("BR_MISSP_TYPE_RETIRED.RETURN", new PMUEvent(null, "0x89", "0,1", "0x4", "BR_MISSP_TYPE_RETIRED.RETURN", "200000", "Mispredicted return branches", null));
		eventMap.put("BR_MISSP_TYPE_RETIRED.IND_CALL", new PMUEvent(null, "0x89", "0,1", "0x8", "BR_MISSP_TYPE_RETIRED.IND_CALL", "200000", "Mispredicted indirect calls, including both register and memory indirect. ", null));
		eventMap.put("BR_MISSP_TYPE_RETIRED.COND_TAKEN", new PMUEvent(null, "0x89", "0,1", "0x11", "BR_MISSP_TYPE_RETIRED.COND_TAKEN", "200000", "Mispredicted and taken cond branch instructions retired", null));
		eventMap.put("INST_RETIRED.ANY_P", new PMUEvent(null, "0xC0", "0,1", "0x0", "INST_RETIRED.ANY_P", "2000000", "Instructions retired (precise event).", null));
		eventMap.put("INST_RETIRED.ANY", new PMUEvent(null, "0xA", "Fixed counter 1", "0x0", "INST_RETIRED.ANY", "2000000", "Instructions retired.", null));
		eventMap.put("UOPS_RETIRED.ANY", new PMUEvent(null, "0xC2", "0,1", "0x10", "UOPS_RETIRED.ANY", "2000000", "Micro-ops retired.", null));
		eventMap.put("UOPS_RETIRED.STALLED_CYCLES", new PMUEvent(null, "0xC2", "0,1", "0x10", "UOPS_RETIRED.STALLED_CYCLES", "2000000", "Cycles no micro-ops retired.", null));
		eventMap.put("UOPS_RETIRED.STALLS", new PMUEvent(null, "0xC2", "0,1", "0x10", "UOPS_RETIRED.STALLS", "2000000", "Periods no micro-ops retired.", null));
		eventMap.put("MACHINE_CLEARS.SMC", new PMUEvent(null, "0xC3", "0,1", "0x1", "MACHINE_CLEARS.SMC", "200000", "Self-Modifying Code detected.", null));
		eventMap.put("BR_INST_RETIRED.ANY", new PMUEvent(null, "0xC4", "0,1", "0x0", "BR_INST_RETIRED.ANY", "2000000", "Retired branch instructions.", null));
		eventMap.put("BR_INST_RETIRED.PRED_NOT_TAKEN", new PMUEvent(null, "0xC4", "0,1", "0x1", "BR_INST_RETIRED.PRED_NOT_TAKEN", "2000000", "Retired branch instructions that were predicted not-taken.", null));
		eventMap.put("BR_INST_RETIRED.MISPRED_NOT_TAKEN", new PMUEvent(null, "0xC4", "0,1", "0x2", "BR_INST_RETIRED.MISPRED_NOT_TAKEN", "200000", "Retired branch instructions that were mispredicted not-taken.", null));
		eventMap.put("BR_INST_RETIRED.PRED_TAKEN", new PMUEvent(null, "0xC4", "0,1", "0x4", "BR_INST_RETIRED.PRED_TAKEN", "2000000", "Retired branch instructions that were predicted taken.", null));
		eventMap.put("BR_INST_RETIRED.MISPRED_TAKEN", new PMUEvent(null, "0xC4", "0,1", "0x8", "BR_INST_RETIRED.MISPRED_TAKEN", "200000", "Retired branch instructions that were mispredicted taken.", null));
		eventMap.put("BR_INST_RETIRED.TAKEN", new PMUEvent(null, "0xC4", "0,1", "0xc", "BR_INST_RETIRED.TAKEN", "2000000", "Retired taken branch instructions.", null));
		eventMap.put("BR_INST_RETIRED.ANY1", new PMUEvent(null, "0xC4", "0,1", "0xf", "BR_INST_RETIRED.ANY1", "2000000", "Retired branch instructions.", null));
		eventMap.put("BR_INST_RETIRED.MISPRED", new PMUEvent(null, "0xC5", "0,1", "0x0", "BR_INST_RETIRED.MISPRED", "200000", "Retired mispredicted branch instructions (precise event).", null));
		eventMap.put("RESOURCE_STALLS.DIV_BUSY", new PMUEvent(null, "0xDC", "0,1", "0x2", "RESOURCE_STALLS.DIV_BUSY", "2000000", "Cycles issue is stalled due to div busy.", null));
		eventMap.put("BR_INST_DECODED", new PMUEvent(null, "0xE0", "0,1", "0x1", "BR_INST_DECODED", "2000000", "Branch instructions decoded", null));
		eventMap.put("BOGUS_BR", new PMUEvent(null, "0xE4", "0,1", "0x1", "BOGUS_BR", "2000000", "Bogus branches", null));
		eventMap.put("BACLEARS.ANY", new PMUEvent(null, "0xE6", "0,1", "0x1", "BACLEARS.ANY", "2000000", "BACLEARS asserted.", null));
		eventMap.put("REISSUE.OVERLAP_STORE", new PMUEvent(null, "0x3", "0,1", "0x1", "REISSUE.OVERLAP_STORE", "200000", "Micro-op reissues on a store-load collision", null));
		eventMap.put("REISSUE.OVERLAP_STORE.AR", new PMUEvent(null, "0x3", "0,1", "0x81", "REISSUE.OVERLAP_STORE.AR", "200000", "Micro-op reissues on a store-load collision (At Retirement)", null));
		eventMap.put("DATA_TLB_MISSES.DTLB_MISS", new PMUEvent(null, "0x8", "0,1", "0x7", "DATA_TLB_MISSES.DTLB_MISS", "200000", "Memory accesses that missed the DTLB.", null));
		eventMap.put("DATA_TLB_MISSES.DTLB_MISS_LD", new PMUEvent(null, "0x8", "0,1", "0x5", "DATA_TLB_MISSES.DTLB_MISS_LD", "200000", "DTLB misses due to load operations.", null));
		eventMap.put("DATA_TLB_MISSES.L0_DTLB_MISS_LD", new PMUEvent(null, "0x8", "0,1", "0x9", "DATA_TLB_MISSES.L0_DTLB_MISS_LD", "200000", "L0 DTLB misses due to load operations.", null));
		eventMap.put("DATA_TLB_MISSES.DTLB_MISS_ST", new PMUEvent(null, "0x8", "0,1", "0x6", "DATA_TLB_MISSES.DTLB_MISS_ST", "200000", "DTLB misses due to store operations.", null));
		eventMap.put("DATA_TLB_MISSES.L0_DTLB_MISS_ST", new PMUEvent(null, "0x8", "0,1", "0xa", "DATA_TLB_MISSES.L0_DTLB_MISS_ST", "200000", "L0 DTLB misses due to store operations", null));
		eventMap.put("PAGE_WALKS.WALKS", new PMUEvent(null, "0xC", "0,1", "0x3", "PAGE_WALKS.WALKS", "200000", "Number of page-walks executed.", null));
		eventMap.put("PAGE_WALKS.CYCLES", new PMUEvent(null, "0xC", "0,1", "0x3", "PAGE_WALKS.CYCLES", "2000000", "Duration of page-walks in core cycles", null));
		eventMap.put("PAGE_WALKS.D_SIDE_WALKS", new PMUEvent(null, "0xC", "0,1", "0x1", "PAGE_WALKS.D_SIDE_WALKS", "200000", "Number of D-side only page walks", null));
		eventMap.put("PAGE_WALKS.D_SIDE_CYCLES", new PMUEvent(null, "0xC", "0,1", "0x1", "PAGE_WALKS.D_SIDE_CYCLES", "2000000", "Duration of D-side only page walks", null));
		eventMap.put("PAGE_WALKS.I_SIDE_WALKS", new PMUEvent(null, "0xC", "0,1", "0x2", "PAGE_WALKS.I_SIDE_WALKS", "200000", "Number of I-Side page walks", null));
		eventMap.put("PAGE_WALKS.I_SIDE_CYCLES", new PMUEvent(null, "0xC", "0,1", "0x2", "PAGE_WALKS.I_SIDE_CYCLES", "2000000", "Duration of I-Side page walks", null));
		eventMap.put("ITLB.HIT", new PMUEvent(null, "0x82", "0,1", "0x1", "ITLB.HIT", "200000", "ITLB hits.", null));
		eventMap.put("ITLB.FLUSH", new PMUEvent(null, "0x82", "0,1", "0x4", "ITLB.FLUSH", "200000", "ITLB flushes.", null));
		eventMap.put("ITLB.MISSES", new PMUEvent(null, "0x82", "0,1", "0x2", "ITLB.MISSES", "200000", "ITLB misses.", null));
		eventMap.put("MEM_LOAD_RETIRED.DTLB_MISS", new PMUEvent(null, "0xCB", "0,1", "0x4", "MEM_LOAD_RETIRED.DTLB_MISS", "200000", "Retired loads that miss the DTLB (precise event).", null));
    }

    public PMUEvent get(String eventName) {
        return eventMap.get(eventName.toUpperCase());
    }

	public void forEach(Consumer<PMUEvent> action) {
		for (PMUEvent event : eventMap.values())
			action.accept(event);
	}
}