package org.pixle.util;

import org.apache.commons.io.IOUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CrashReport {
    public String description;
    public Throwable throwable;

    private CrashReport(String description, Throwable throwable) {
        this.description = description;
        this.throwable = throwable;
    }

    public static String makeCrashReport(Throwable e, String d) {
        return new CrashReport(d, e).getCompleteReport();
    }

    private String getCompleteReport() {
        return "---- Crash Report ----\nDescription: " + description + "\n\n-- Crash Log --\n" + getStackTrace() + "\n-- System Details --\n" + getSystemDetails();
    }

    private String getSystemDetails() {
        String s = "";
        s += toPrettyString("Current Thread", Thread.currentThread().getName());
        s += toPrettyString("Operating System", System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ")");
        s += toPrettyString("Java Version", System.getProperty("java.version"));
        if (Side.get().isClient()) {
            s += toPrettyString("LWJGL Version", org.lwjgl.Sys.getVersion());
        }
        return s;
    }

    private String toPrettyString(String key, String value) {
        return "\t" + key + ": " + value + "\n";
    }

    private String getStackTrace() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        throwable.printStackTrace(printWriter);
        String s = stringWriter.toString();

        IOUtils.closeQuietly(stringWriter);
        IOUtils.closeQuietly(printWriter);

        return s;
    }
}