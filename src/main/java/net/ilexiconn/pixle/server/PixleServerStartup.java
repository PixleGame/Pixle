package net.ilexiconn.pixle.server;

import com.esotericsoftware.minlog.Log;
import net.ilexiconn.pixle.util.StartupUtils;
import net.ilexiconn.pixle.util.SystemUtils;

import java.io.File;
import java.util.Map;

public class PixleServerStartup {
    public static void main(String[] args) {
        Thread.currentThread().setName("Server");
        Map<String, String> properties = StartupUtils.argsToMap(args);
        SystemUtils.setGameFolder(new File(properties.get("gamefolder")));
        Log.info("Server", "Starting server on port " + properties.get("port"));
        PixleServer server = new PixleServer();
        server.start(Integer.parseInt(properties.get("port")));
    }
}
