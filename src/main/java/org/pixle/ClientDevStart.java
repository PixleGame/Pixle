package org.pixle;

import com.esotericsoftware.minlog.Log;
import org.pixle.util.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

public class ClientDevStart extends DevStart {
    public static void main(String[] args) throws ReflectiveOperationException {
        new ClientDevStart().start(args);
    }

    @Override
    public void preInit(Map<String, String> properties) {
        File gameFolder = new File(properties.get("gamefolder"));
        try {
            LWJGLSetup.load(new File(gameFolder, "natives"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        applyDefault(properties, "username", "Player" + (int) (Math.random() * 1000L));
        applyDefault(properties, "gamefolder", SystemUtils.getGameFolder().getAbsolutePath());
        applyDefault(properties, "log", "info");
    }

    @Override
    public String getStartupClassName() {
        return "PixleClientStartup";
    }
}
