package org.pixle.server;

import com.esotericsoftware.minlog.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FilenameUtils;
import org.pixle.client.PixleClient;
import org.pixle.event.bus.EventBus;
import org.pixle.plugin.PluginContainer;
import org.pixle.plugin.PluginContainerAdapter;
import org.pixle.util.PixleLogger;
import org.pixle.util.StartupUtils;
import org.pixle.util.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PixleServerStartup {
    private static final Gson pluginGson = new GsonBuilder().registerTypeAdapter(PluginContainer.class, new PluginContainerAdapter()).create();

    public static void main(String[] args) {
        Thread.currentThread().setName("Server");
        Log.setLogger(new PixleLogger());
        Map<String, String> properties = StartupUtils.argsToMap(args);
        SystemUtils.setGameFolder(new File(properties.get("gamefolder")));
        Log.info("Server", "Starting server on port " + properties.get("port"));
        PixleServer server = new PixleServer();
        loadPlugins(server);
        server.start(Integer.parseInt(properties.get("port")));
    }

    private static void loadPlugins(PixleServer server) {
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
                            PluginContainer plugin = pluginGson.fromJson(new InputStreamReader(stream), PluginContainer.class);
                            server.pluginList.add(plugin);
                            EventBus.INSTANCE.register(plugin.getInstance());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        InputStream stream = PixleClient.class.getResourceAsStream("/plugin.json");
        if (stream != null) {
            PluginContainer plugin = pluginGson.fromJson(new InputStreamReader(stream), PluginContainer.class);
            server.pluginList.add(plugin);
            EventBus.INSTANCE.register(plugin.getInstance());
        }
    }
}
