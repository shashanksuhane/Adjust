package com.suhane.lib_core.event;

public class Event {
    private long timeInSec;

    public Event(long timeInSec) {
        this.timeInSec = timeInSec;
    }

    public long getTimeInSec() {
        return timeInSec;
    }
}
