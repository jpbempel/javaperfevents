package com.bempel.perf.jna;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Tracepoint {
    private static final Map<String, TracepointInfo> tracepointMap = new HashMap<>();
    static {
        init();
    }

    public static void init() {
        File tracingDir = new File("/sys/kernel/debug/tracing/events");
        if (!tracingDir.exists()) {
            throw new RuntimeException("Cannot list Tracepoints");
        }
        if (!tracingDir.canExecute()) {
            throw new RuntimeException("cannot execute " + tracingDir.getAbsolutePath());
        }
        try {
            Files.list(tracingDir.toPath()).forEach(categoryPath -> {
                File categoryFile = new File(String.format("/sys/kernel/debug/tracing/events/%s", categoryPath.getFileName()));
                if (categoryFile.exists() && categoryFile.isDirectory()) {
                    try {
                        Files.list(categoryFile.toPath()).forEach(tracepointPath -> {
                            File idFile = new File(String.format("/sys/kernel/debug/tracing/events/%s/%s/id", categoryPath.getFileName(), tracepointPath.getFileName()));
                            if (idFile.exists()) {
                                try {
                                    List<String> lines = Files.readAllLines(idFile.toPath());
                                    if (!lines.isEmpty()) {
                                        int id = Integer.parseInt(lines.get(0));
                                        String tracepointName = categoryPath.getFileName() + ":" + tracepointPath.getFileName();
                                        tracepointMap.put(tracepointName.toUpperCase(), new TracepointInfo(id, tracepointName));
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void forEach(Consumer<TracepointInfo> action) {
        tracepointMap.entrySet().stream().sorted(Comparator.comparing(o -> o.getValue().name)).forEach(entry -> {
            action.accept(entry.getValue());
        });
    }

    public static TracepointInfo get(String name) {
        return tracepointMap.get(name.toUpperCase());
    }

    public static class TracepointInfo {
        private final int id;
        private final String name;

        public TracepointInfo(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
