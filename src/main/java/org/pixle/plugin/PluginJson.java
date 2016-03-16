package org.pixle.plugin;

public class PluginJson {
    private String mainClass;
    private String id;
    private String name;
    private String version;
    private Object instance;

    public String getMainClass() {
        return mainClass;
    }

    public String getId() {
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
