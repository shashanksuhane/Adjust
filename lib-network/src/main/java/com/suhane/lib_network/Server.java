package com.suhane.lib_network;

import com.suhane.lib_core.event.Event;
import com.suhane.lib_core.result.Result;

public interface Server {
    Result post(Event event);
}
