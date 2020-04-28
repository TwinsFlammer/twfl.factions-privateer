package com.redefocus.factionscaribe.mcmmo.util;

import java.util.logging.Filter;
import java.util.logging.LogRecord;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LogFilter implements Filter {

    private boolean debug;

    @Override
    public boolean isLoggable(LogRecord record) {
        return !(record.getMessage().contains("[Debug]") && !debug);
    }
}
