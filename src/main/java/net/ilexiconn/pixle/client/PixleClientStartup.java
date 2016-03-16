package net.ilexiconn.pixle.client;

import com.esotericsoftware.minlog.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ilexiconn.pixle.event.bus.EventBus;
import net.ilexiconn.pixle.plugin.PluginJson;
import net.ilexiconn.pixle.plugin.PluginJsonAdapter;
import net.ilexiconn.pixle.util.StartupUtils;
import net.ilexiconn.pixle.util.SystemUtils;
import org.apache.commons.io.FilenameUtils;
import org.lwjgl.Sys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PixleClientStartup {
    private static final Gson pluginGson = new GsonBuilder().registerTypeAdapter(PluginJson.class, new PluginJsonAdapter()).create();

    public static void main(String[] args) {
        Thread.currentThread().setName("Client");
        Map<String, String> properties = StartupUtils.argsToMap(args);
        SystemUtils.setGameFolder(new File(properties.get("gamefolder")));
        Log.info("Startup", "Setting user: " + properties.get("username"));
        Log.info("Startup", "LWJGL version: " + Sys.getVersion());
        PixleClient client = new PixleClient();
        loadPlugins(client);
        client.start(properties);
    }

    private static void loadPlugins(PixleClient client) {
        File modDir = new File(SystemUtils.getGameFolder(), "plugins");
        if (!modDir.exists()) {
            modDir.mkdir();
        }
        for (File file : modDir.listFiles()) {
            if (FilenameUtils.getExtension(file.getAbsolutePath()).equals("pxlm")) {
                try {
                    ZipFile zipFile = new ZipFile(file);
                    Enumeration<? extends ZipEntry> entries = zipFile.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry zipEntry = entries.nextElement();
                        if (zipEntry.getName().equals("plugin.json")) {
                            InputStream stream = zipFile.getInputStream(zipEntry);
                            PluginJson plugin = pluginGson.fromJson(new InputStreamReader(stream), PluginJson.class);
                            client.pluginList.add(plugin);
                            EventBus.get().register(plugin.getInstance());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        InputStream stream = PixleClient.class.getResourceAsStream("/plugin.json");
        if (stream != null) {
            PluginJson plugin = pluginGson.fromJson(new InputStreamReader(stream), PluginJson.class);
            client.pluginList.add(plugin);
            EventBus.get().register(plugin.getInstance());
        }
    }
}
