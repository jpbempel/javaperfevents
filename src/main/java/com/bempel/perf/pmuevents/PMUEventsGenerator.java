package com.bempel.perf.pmuevents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class source generator for PMU events per cpu model
 * Helps to map pmu event names to specific encodings
 * This is like perf command line tool is doing using definitions stored in
 * linux/tools/perf/pmu-events/ in linux source repository
 */
public class PMUEventsGenerator {
    private static List<CpuInfo> cpus = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            printHelp();
            return;
        }
        String srcDir = args[0];
        String destDir = args[1];
        processMapFile(srcDir, destDir);
        cpus.forEach(cpuInfo -> processCpuModel(srcDir, destDir, cpuInfo));
    }

    private static void processMapFile(String srcDir, String destDir) throws Exception {
        File mapFile = new File(srcDir, "mapfile.csv");
        if (!mapFile.exists())
            throw new RuntimeException("Mapfile " + mapFile.getAbsolutePath() + " does not exist");
        try (BufferedReader reader = new BufferedReader(new FileReader(mapFile))) {
            reader.lines().forEach(PMUEventsGenerator::mapFileLine); // fill cpus
        }
        StringBuilder sb = new StringBuilder();
        cpus.stream().forEach(cpuInfo -> {
            sb.append("\t\tcpuIdMap.put(\"");
            sb.append(cpuInfo.familyModel);
            sb.append("\", \"");
            sb.append(escapeClassName(cpuInfo.filename));
            sb.append("\");\n");
        });
        String initEventContent = sb.toString();
        String classTemplate = getFileStringContent("/PMUEvents.txt");
        String destFileContent = String.format(classTemplate, initEventContent);
        writeFileContent(destDir, "PMUEvents", destFileContent);
    }

    private static void mapFileLine(String line) {
        String[] csvValues = line.split(",");
        if (csvValues.length != 4) {
            throw new RuntimeException("Cannot parse the line: " + line);
        }
        if (csvValues[0].equals("Family-model")) { // skip header
            return;
        }
        cpus.add(new CpuInfo(csvValues[0], csvValues[1], csvValues[2], csvValues[3]));
    }

    private static void processCpuModel(String srcDir, String destDir, CpuInfo cpuInfo) {
        File archDirFile = new File(new File(srcDir, "x86"), cpuInfo.filename);
        if (!archDirFile.exists()) {
            throw new RuntimeException("Cannot process the directory: " + archDirFile.getAbsolutePath());
        }
        String className = escapeClassName(cpuInfo.filename);
        StringBuilder sb = new StringBuilder();
        try {
            Files.list(archDirFile.toPath())
                    .filter(path -> !path.toString().contains("-metrics"))
                    .forEach(jsonFileName -> processJsonPMUFile(sb, jsonFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String initEventContent = sb.toString();
        String eventClassTemplate = getFileStringContent("/EventClassTemplate.txt");
        String destFileContent = String.format(eventClassTemplate, className, initEventContent);
        writeFileContent(destDir, className, destFileContent);
    }

    private static void writeFileContent(String destDir, String className, String destFileContent) {
        try (FileWriter writer = new FileWriter(new File(destDir, className + ".java"))) {
            writer.write(destFileContent, 0, destFileContent.length());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void processJsonPMUFile(StringBuilder sb, Path jsonFileName) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PMUEvent[] pmuEvents = mapper.readValue(jsonFileName.toFile(), PMUEvent[].class);
            Arrays.stream(pmuEvents).forEach(pmuEvent -> {
                sb.append("\t\teventMap.put(\"").append(pmuEvent.EventName).append("\", new PMUEvent(");
                sb.append(pmuEvent.PublicDescription != null ?  "\"" + escape(pmuEvent.PublicDescription) + "\"" : "null");
                sb.append(", ");
                sb.append(pmuEvent.EventCode != null ?  "\"" + escape(pmuEvent.EventCode) + "\"": "null");
                sb.append(", ");
                sb.append(pmuEvent.Counter != null ?  "\"" + escape(pmuEvent.Counter) + "\"": "null");
                sb.append(", ");
                sb.append(pmuEvent.UMask != null ?  "\"" + escape(pmuEvent.UMask) + "\"": "null");
                sb.append(", ");
                sb.append(pmuEvent.EventName != null ?  "\"" + escape(pmuEvent.EventName) + "\"" : "null");
                sb.append(", ");
                sb.append(pmuEvent.SampleAfterValue != null ?  "\"" + escape(pmuEvent.SampleAfterValue) + "\"": "null");
                sb.append(", ");
                sb.append(pmuEvent.BriefDescription != null ?  "\"" + escape(pmuEvent.BriefDescription) + "\"": "null");
                sb.append(", ");
                sb.append(pmuEvent.CounterHTOff != null ?  "\"" + escape(pmuEvent.CounterHTOff) + "\"": "null");
                sb.append("));\n");
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileStringContent(String fileName) {
        try {
            URL resource = PMUEventsGenerator.class.getResource(fileName);
            Path eventClassTemplatePath = Paths.get(resource.toURI());
            return String.join("\n", Files.readAllLines(eventClassTemplatePath));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String escape(String s) {
        s = s.replaceAll("[\r\n]", "");
        s = s.replaceAll("\"", "\\\\\"");
        return s;
    }

    private static String escapeClassName(String s) {
        if (s == null || s.isEmpty())
            return s;
        s = s.replaceAll("-", "_");
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private static void printHelp() {
        System.out.println("Class source generator for PMU events per cpu model");
        System.out.println(PMUEventsGenerator.class.getName() + " <source directory> <destination dir>");
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PMUEvent {
        public String EventCode;
        public String UMask;
        public String PublicDescription;
        public String BriefDescription;
        public String Counter;
        public String EventName;
        public String SampleAfterValue;
        public String CounterHTOff;
    }

    private static class CpuInfo {
        final String familyModel;
        final String version;
        final String filename;
        final String eventType;

        public CpuInfo(String familyModel, String version, String filename, String eventType) {
            this.familyModel = familyModel;
            this.version = version;
            this.filename = filename;
            this.eventType = eventType;
        }
    }
}