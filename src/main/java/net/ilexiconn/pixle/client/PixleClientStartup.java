package net.ilexiconn.pixle.client;

import com.esotericsoftware.minlog.Log;
import net.ilexiconn.pixle.util.StartupUtils;
import net.ilexiconn.pixle.util.SystemUtils;
import org.lwjgl.Sys;

import java.io.File;
import java.util.Map;

public class PixleClientStartup {
    public static void main(String[] args) {
        Thread.currentThread().setName("Client");
        Map<String, String> properties = StartupUtils.argsToMap(args);
        SystemUtils.setGameFolder(new File(properties.get("gamefolder")));
        Log.info("Startup", "Setting user: " + properties.get("username"));
        Log.info("Startup", "LWJGL version: " + Sys.getVersion());
        PixleClient client = new PixleClient();
        client.start(properties);
    }
}
