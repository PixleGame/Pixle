package org.pixle;

import com.esotericsoftware.minlog.Log;
import org.pixle.util.SystemUtils;

import java.lang.reflect.Field;
import java.util.Map;

public class ServerDevStart extends DevStart {
    public static void main(String[] args) throws ReflectiveOperationException {
        new ServerDevStart().start(args);
    }

    @Override
    public void preInit(Map<String, String> properties) {
        int level = Log.LEVEL_INFO;
        try {
            Field field = Log.class.getField("LEVEL_" + properties.get("log").toUpperCase());
            level = field.getInt(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.set(level);
    }

    @Override
    public void applyDefaults(Map<String, String> properties) {
        applyDefault(properties, "port", 25565);
        applyDefault(properties, "gamefolder", SystemUtils.getGameFolder().getAbsolutePath());
        applyDefault(properties, "log", "info");
    }

    @Override
    public String getStartupClassName() {
        return "org.pixle.server.PixleServerStartup";
    }
}
