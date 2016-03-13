package net.ilexiconn.pixle.client;

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
        System.out.println("Setting user: " + properties.get("username"));
        System.out.println("LWJGL version: " + Sys.getVersion());
        PixleClient client = new PixleClient();
        client.start(properties.get("username"), properties.get("host"), Integer.parseInt(properties.get("port")));
    }
}
