package com.suhane.log_sdk.api;

import com.suhane.lib_core.result.Result;

public interface LogApi {
    Result init();
    Result send(long currentTimeInSec);
}
