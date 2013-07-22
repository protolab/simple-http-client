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

import java.lang.reflect.Field;

/**
 * Misc. utility methods to facilitate logging.
 *
 * @author Michael Krasnovsky <a
 *         href="mailto:mkrasnovsky@gmail.com">mkrasnovsky@gmail.com</a>
 */
public final class LogUtils {

    private static final String MSG_IN_BOUND_ENTRY_FMT = "%d > %s";
    private static final String MSG_OUT_BOUND_ENTRY_FMT = "%d < %s";
    private static final String MSG_IN_BOUND_REC_FMT = "%d * Server in-bound request";
    private static final String MSG_OUT_BOUND_REC_FMT = "%d * Server out-bound response";

    private LogUtils() {
        throw new IllegalStateException();
    }

    /**
     * Dumps an object state. If field value cannot be read, "UNKNOWN" as the
     * field value is used.
     *
     * @param o an object to inspect.
     * @return a string containing field name and value pairs separated by
     *         comma.
     */
    public static String dump(Object o) {
        if (o == null) {
            return "[NULL]";
        }

        StringBuilder b = new StringBuilder();
        b.append(o.getClass().getName());
        b.append(" [");

        Field[] fields = o.getClass().getDeclaredFields();
        for (int i = 0, l = fields.length; i < l; i++) {
            if (i > 0) {
                b.append(", ");
            }
            fields[i].setAccessible(true);
            b.append(fields[i].getName());
            b.append("=");
            try {
                Object value = fields[i].get(o);
                b.append(value);
            }
            catch (Exception e) {
                b.append("UNKNOWN");
            }
        }
        b.append("]");
        return b.toString();
    }

    /**
     * Formats HTTP request data.
     *
     * @param req HTTP request.
     * @return formatted string.
     */
//    public static String format(HttpRequest req) {
//        StringWriter sw = new StringWriter();
//        PrintWriter pw = new PrintWriter(sw);
//
//        long tid = Thread.currentThread().getId();
//        pw.println(String.format(MSG_IN_BOUND_REC_FMT, tid));
//        String first = String.format("%s %s", req.getMethod(), req.getURI());
//        pw.println(String.format(MSG_IN_BOUND_ENTRY_FMT, tid, first));
//
//        writeHeaders(tid, pw, req.getHeaders(), MSG_IN_BOUND_ENTRY_FMT);
//
//        pw.close();
//        return sw.toString();
//    }

    /**
     * Formats HTTP response code and response headers.
     *
     * @param respCode HTTP response status code.
     * @param headers  HTTP response headers.
     * @return formatted string.
     */
//    public static String format(int respCode, Map<String, List<String>> headers) {
//        StringWriter sw = new StringWriter();
//        PrintWriter pw = new PrintWriter(sw);
//
//        long tid = Thread.currentThread().getId();
//        pw.println(String.format(MSG_OUT_BOUND_REC_FMT, tid));
//        pw.println(String.format(MSG_OUT_BOUND_ENTRY_FMT, tid, respCode));
//
//        writeHeaders(tid, pw, headers, MSG_OUT_BOUND_ENTRY_FMT);
//
//        pw.close();
//        return sw.toString();
//    }

//    @SuppressWarnings("boxing")
//    private static void writeHeaders(
//            long threadId,
//            PrintWriter pw,
//            Map<String, List<String>> headers,
//            String entryFmt) {
//
//        try (Scanner s = new Scanner(Headers.toString(headers))) {
//            s.useDelimiter(System.lineSeparator());
//            while (s.hasNext()) {
//                String line = String.format(entryFmt, threadId, s.next());
//                pw.println(line);
//            }
//            pw.println(String.format(entryFmt, threadId, ""));
//        }
//    }
}
