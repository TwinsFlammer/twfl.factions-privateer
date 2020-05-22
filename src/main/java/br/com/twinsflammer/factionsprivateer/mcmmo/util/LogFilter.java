package br.com.twinsflammer.factionsprivateer.mcmmo.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

@NoArgsConstructor
@AllArgsConstructor
public class LogFilter implements Filter {

    private boolean debug;

    @Override
    public boolean isLoggable(LogRecord record) {
        return !(record.getMessage().contains("[Debug]") && !debug);
    }
}
