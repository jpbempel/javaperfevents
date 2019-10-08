package com.bempel.perf.pmuevents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CPUID {
    private static String VENDOR;
    private static int FAMILY;
    private static int MODEL;

    static {
        parseCPUInfo();
    }

    public static String getCpuId() {
        return String.format("%s-%X-%X", VENDOR, FAMILY, MODEL);
    }

    public static String getVendor() {
        return VENDOR;
    }

    public static int getFamily() {
        return FAMILY;
    }

    public static int getModel() {
        return MODEL;
    }

    private static void parseCPUInfo() {
        File cpuinfoFile = new File("/proc/cpuinfo");
        if (!cpuinfoFile.exists() || !cpuinfoFile.canRead())
            throw new UnsupportedOperationException("Cannot read file /proc/cpuinfo");
        String vendor = null;
        int family = 0;
        int model = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(cpuinfoFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("vendor_id")) {
                    vendor = extractLineValue(line);
                }
                if (line.startsWith("cpu family")) {
                    family = Integer.parseInt(extractLineValue(line));
                }
                if (line.startsWith("model\t")) {
                    model = Integer.parseInt(extractLineValue(line));
                }
                if (vendor != null && family != 0 && model != 0) {
                    break;
                }
            }
            VENDOR = vendor;
            FAMILY = family;
            MODEL = model;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String extractLineValue(String line) {
        String[] values = line.split(": ");
        if (values.length == 2)
            return values[1];
        throw new RuntimeException("Cannot parse the line: " + line);
    }
}
