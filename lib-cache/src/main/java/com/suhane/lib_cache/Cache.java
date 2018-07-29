package com.suhane.lib_cache;

import com.suhane.lib_core.event.Event;

public interface Cache {
    boolean init();
    boolean save(Event event);
    Event getNextPendingEvent();
    boolean delete(Event event);
    boolean isDuplicate(Event event);
}
