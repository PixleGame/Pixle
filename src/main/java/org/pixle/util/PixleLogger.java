package org.pixle.util;

import com.esotericsoftware.minlog.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PixleLogger extends Log.Logger {
    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void log(int level, String category, String message, Throwable throwable) {
        StringBuilder builder = new StringBuilder();

        builder.append("[").append(PixleLogger.DATE_FORMATTER.format(new Date())).append("][");
        switch (level) {
            case Log.LEVEL_ERROR:
                builder.append("error");
                break;
            case Log.LEVEL_WARN:
                builder.append("warn");
                break;
            case Log.LEVEL_INFO:
                builder.append("info");
                break;
            case Log.LEVEL_DEBUG:
                builder.append("debug");
                break;
            case Log.LEVEL_TRACE:
                builder.append("trace");
                break;
        }
        builder.append("/").append(Side.get()).append("]");
        if (category != null) {
            builder.append("[").append(category).append("]");
        }
        builder.append(": ").append(message);
        if (throwable != null) {
            StringWriter writer = new StringWriter();
            throwable.printStackTrace(new PrintWriter(writer));
            builder.append('\n');
            builder.append(writer.toString().trim());
        }

        this.print(builder.toString());
    }
}
