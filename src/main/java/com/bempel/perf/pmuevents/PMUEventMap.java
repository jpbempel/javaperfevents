package com.bempel.perf.pmuevents;

import java.util.function.Consumer;

public interface PMUEventMap {
    PMUEvent get(String eventName);
    void forEach(Consumer<PMUEvent> action);
}
