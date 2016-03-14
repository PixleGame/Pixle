import net.ilexiconn.pixle.util.StartupUtils;

import java.util.Map;

public abstract class DevStart {
    public void start(String[] args) throws ReflectiveOperationException {
        Map<String, String> properties = StartupUtils.argsToMap(args);
        applyDefaults(properties);
        preInit(properties);
        Class<?> clazz = Class.forName(getStartupClassName());
        clazz.getMethod("main", String[].class).invoke(null, new Object[]{
                StartupUtils.mapToArgs(properties)
        });
    }

    public abstract String getStartupClassName();

    public void preInit(Map<String, String> properties) {

    }

    public void applyDefaults(Map<String, String> properties) {

    }

    public static void applyDefault(Map<String, String> properties, String key, Object value) {
        if (!properties.containsKey(key)) {
            properties.put(key, String.valueOf(value));
        }
    }
}
