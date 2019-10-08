package com.bempel.perf.pmuevents;

import java.util.HashMap;
import java.util.Map;

public class PMUEvent {
    public final String publicDescription;
    public final String eventCode;
    public final String counter;
    public final String umask;
    public final String eventName;
    public final String sampleAfterValue;
    public final String briefDescription;
    public final String counterHTOff;
    public PMUEvent(String publicDescription, String eventCode, String counter, String umask, String eventName, String sampleAfterValue, String briefDescription, String counterHTOff) {
        this.publicDescription = publicDescription;
        this.eventCode = eventCode;
        this.counter = counter;
        this.umask = umask;
        this.eventName = eventName;
        this.sampleAfterValue = sampleAfterValue;
        this.briefDescription = briefDescription;
        this.counterHTOff = counterHTOff;
    }
}
