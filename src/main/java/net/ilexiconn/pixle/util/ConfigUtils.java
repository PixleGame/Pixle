package net.ilexiconn.pixle.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ilexiconn.pixle.crash.CrashReport;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class ConfigUtils {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T> T loadConfig(File f, Class<T> t) {
        if (!f.exists()) {
            try {
                T i = t.newInstance();;
                String json = gson.toJson(i);
                f.createNewFile();
                IOUtils.write(json.getBytes(), new FileOutputStream(f));
                return i;
            } catch (Exception e) {
                System.err.println(CrashReport.makeCrashReport(e, "Failed making a new instance of " + t.getName()));
                return null;
            }
        } else {
            try {
                return gson.fromJson(new FileReader(f), t);
            } catch (FileNotFoundException e) {
                System.err.println(CrashReport.makeCrashReport(e, "Couldn't find config file " + f.getName()));
                return null;
            }
        }
    }

    public static <T> T saveConfig(T t, File f) {
        String json = gson.toJson(t);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            IOUtils.write(json.getBytes(), new FileOutputStream(f));
        } catch (IOException e) {
            System.err.println(CrashReport.makeCrashReport(e, "Couldn't write to file " + f.getName()));
        }
        return t;
    }
}
