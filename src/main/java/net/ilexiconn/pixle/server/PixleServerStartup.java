package net.ilexiconn.pixle.server;

import net.ilexiconn.pixle.Startup;
import net.ilexiconn.pixle.util.SystemUtils;

import java.io.File;
import java.util.Map;

public class PixleServerStartup {
    public static void main(String[] args) {
        Thread.currentThread().setName("Server");
        Map<String, String> properties = Startup.argsToMap(args);
        SystemUtils.setGameFolder(new File(properties.get("gamefolder")));
        PixleServer server = new PixleServer();
        server.start(Integer.parseInt(properties.get("port")));
    }
}
