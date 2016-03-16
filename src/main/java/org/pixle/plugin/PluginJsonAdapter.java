package org.pixle.plugin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class PluginJsonAdapter extends TypeAdapter<PluginJson> {
    public Gson gson = new Gson();

    @Override
    public void write(JsonWriter out, PluginJson value) throws IOException {

    }

    @Override
    public PluginJson read(JsonReader in) throws IOException {
        JsonObject object = gson.getAdapter(JsonElement.class).read(in).getAsJsonObject();
        PluginJson plugin = gson.fromJson(object, PluginJson.class);
        try {
            plugin.setInstance(Class.forName(plugin.getMainClass()).newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Couldn't find main class for plugin " + plugin.getId() + " (" + plugin.getMainClass() + ")");
        }
        return plugin;
    }
}
