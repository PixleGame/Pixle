package org.pixle.plugin;

import com.google.gson.annotations.SerializedName;

public class PluginContainer {
    @SerializedName("main_class")
    private String mainClass;
    private String id;
    private String name;
    private String version;
    private Object instance;

    public String getMainClass() {
        return mainClass;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}
