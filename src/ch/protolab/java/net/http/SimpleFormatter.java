/*
 *  Copyright 2012 Michael Krasnovsky
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package ch.protolab.java.net.http;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Formats the given log records. Uses the following template:<br/>
 * <code>[ISO date and time] [ThreadID] [Level] [Logger name] message\nstack trace</code>
 * <br/>
 * <br/>
 * {@code SimpleFormatter} could be set in the logging configuration file:<br/>
 * <code>-Djava.util.logging.config.file=&lt;a properties file&gt;</code><br/>
 * <br/>
 * Here is an example of {@code logging.propeties}<br/>
 * <br/>
 * <code>
 * handlers = java.util.logging.ConsoleHandler<br/>
 * .level = INFO<br/>
 * java.util.logging.ConsoleHandler.level = FINEST<br/>
 * java.util.logging.ConsoleHandler.formatter = ch.protolab.util.logging.SimpleFormatter<br/>
 * ch.protolab.rest.level = FINEST
 * </code>
 *
 * @author Michael Krasnovsky <a
 *         href="mailto:mkrasnovsky@gmail.com">mkrasnovsky@gmail.com</a>
 */
public final class SimpleFormatter extends Formatter {

    static final String ISO_DATE_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    static final String REC_TEMPLATE = "[%s] [ThreadID-%d] [%s] [%s] %s";

    private static final SimpleDateFormat format = new SimpleDateFormat(ISO_DATE_FMT);

    /*
     * (non-Javadoc)
     * 
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    @SuppressWarnings("boxing")
    @Override
    public String format(LogRecord rec) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        String now = format.format(new Date(rec.getMillis()));
        int threadId = rec.getThreadID();
        Level level = rec.getLevel();
        String loggerName = rec.getLoggerName();
        String msg = formatMessage(rec);

        pw.print(String.format(REC_TEMPLATE, now, threadId, level, loggerName, msg));
        pw.println();

        if (rec.getThrown() != null) {
            rec.getThrown().printStackTrace(pw);
        }
        pw.close();
        return sw.toString();
    }
}
