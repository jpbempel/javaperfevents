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
