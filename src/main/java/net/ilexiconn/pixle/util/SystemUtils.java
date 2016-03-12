package net.ilexiconn.pixle.util;

import java.io.File;

public final class SystemUtils {
    public enum OperatingSystem {
        WINDOWS, LINUX, MACOSX, SOLARIS, UNKNOWN;
    }

    private static File gameFolder;

    public static OperatingSystem getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return OperatingSystem.WINDOWS;
        } else if (os.contains("sunos") || os.contains("solaris")) {
            return OperatingSystem.SOLARIS;
        } else if (os.contains("unix")) {
            return OperatingSystem.LINUX;
        } else if (os.contains("linux")) {
            return OperatingSystem.LINUX;
        } else if (os.contains("mac")) {
            return OperatingSystem.MACOSX;
        } else {
            return OperatingSystem.UNKNOWN;
        }
    }

    public static File getGameFolder() {
        if (gameFolder == null) {
            String appdata = System.getenv("APPDATA");
            if (appdata != null) {
                gameFolder = new File(appdata, ".pixle");
            } else {
                gameFolder = new File(System.getProperty("user.home"), ".pixle");
            }
        }
        return gameFolder;
    }

    public static void setGameFolder(File file) {
        gameFolder = file;
    }
}
