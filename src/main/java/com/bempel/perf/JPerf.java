package com.bempel.perf;

import com.bempel.perf.jna.PerfEventConsts;
import com.bempel.perf.pmuevents.PMUEventMap;
import com.bempel.perf.pmuevents.PMUEvents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class JPerf {
    private static final Map<String, Consumer<String[]>> actions = new HashMap<>();

    private static void print(String name, long value) {
        System.out.println(name + ": " + value);
    }

    public static void main(String[] args) {
        //PerfEvent.installLogger(msg -> System.out.print(msg.get()));
        actions.put("list", JPerf::list);
        actions.put("stat", JPerf::stat);
        if (args.length == 0 || "help".equals(args[0])) {
            printHelp();
            return;
        }
        String actionName = args[0];
        Consumer<String[]> action = actions.get(actionName);
        if (action == null) {
            throw new RuntimeException("Unknown action: " + actionName);
        }
        action.accept(args);
    }

    private static void printHelp() {
        System.out.println("JPerf: Java wrapper of perf tool.");
        System.out.println("java " + JPerf.class.getName() + " <action>");
    }

    private static void list(String[] args) {
        System.out.println("Hardware events:");
        for (PerfEventConsts.PerfHwId id : PerfEventConsts.PerfHwId.values()) {
            System.out.println("\t" + id.getName());
        }
        System.out.println("\nHardware cache events:");
        for (PerfEventConsts.PerfHwCacheId id : PerfEventConsts.PerfHwCacheId.values()) {
            System.out.println("\t" + id.getName());
        }
        System.out.println("\nSoftware events:");
        for (PerfEventConsts.PerfSwId id : PerfEventConsts.PerfSwId.values()) {
            System.out.println("\t" + id.getName());
        }
        PMUEventMap eventMap = PMUEvents.getPMUEventMap();
        System.out.printf("\nPMU Events: [Arch: %s]\n", eventMap.getClass().getSimpleName());
        eventMap.forEach(pmuEvent -> {
            System.out.println(pmuEvent.eventName + ":");
            System.out.println("\tCounter: " + pmuEvent.counter);
            System.out.println("\tPublicDescription: " + pmuEvent.publicDescription);
            System.out.println("\tBriefDescription: " + pmuEvent.briefDescription);
            System.out.println("\tEventCode: " + pmuEvent.eventCode);
            System.out.println("\tUMask: " + pmuEvent.umask);
            System.out.println("\tSampleAfterValue: " + pmuEvent.sampleAfterValue);
            System.out.println("\tCounterHTOff: " + pmuEvent.counterHTOff);
        });
        // TODO list tracepoints in /sys/kernel/debug/tracing/
    }

    private static void stat(String[] args) {
        List<PerfEvent[]> perfEvents = new ArrayList<>();
        int nbCpu = Runtime.getRuntime().availableProcessors();
        if (args.length == 1) { // no additional args => default
            initPerfEvent(perfEvents, nbCpu, "cpu-clock");
            initPerfEvent(perfEvents, nbCpu,"context-switches");
            initPerfEvent(perfEvents, nbCpu,"cpu-migrations");
            initPerfEvent(perfEvents, nbCpu,"page-faults");
            initPerfEvent(perfEvents, nbCpu,"cycles");
            initPerfEvent(perfEvents, nbCpu,"instructions");
            initPerfEvent(perfEvents, nbCpu,"branches");
            initPerfEvent(perfEvents, nbCpu,"branch-misses");
        } else if (args.length >= 3 && args[1].equals("-e")) {
            String eventName = args[2];
            initPerfEvent(perfEvents, nbCpu, eventName);
        } else
            throw new UnsupportedOperationException("invalid argument");
        perfEvents.forEach(perfEventPerCpu -> {
            for (int i = 0; i < perfEventPerCpu.length; i++) {
                perfEventPerCpu[i].start(-1, i);
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        perfEvents.forEach(perfEventPerCpu -> {
            long res = Arrays.stream(perfEventPerCpu).mapToLong(PerfEvent::read).sum();
            System.out.printf("%s: %,d\n", perfEventPerCpu[0].getEventName(), res);
        });
    }

    private static void initPerfEvent(List<PerfEvent[]> perfEvents, int nbCpu, String eventName) {
        PerfEvent[] perfEventPerCpu = new PerfEvent[nbCpu];
        for (int i = 0; i < perfEventPerCpu.length; i++)
            perfEventPerCpu[i] = new PerfEvent(eventName);
        perfEvents.add(perfEventPerCpu);
    }
}
