package net.ilexiconn.pixle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Startup {
    public static Map<String, String> argsToMap(String[] args) {
        String current = null;
        HashMap<String, String> properties = new HashMap<>();
        for (String arg : args) {
            if (arg.startsWith("--")) {
                if (current != null && !properties.containsKey(current)) {
                    properties.put(current, "");
                }
                current = arg.substring(2);
            } else {
                properties.put(current, arg);
            }
        }
        if (current != null && !properties.containsKey(current)) {
            properties.put(current, "");
        }

        return properties;
    }

    public static String[] mapToArgs(Map<String, String> map) {
        List<String> stringList = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            stringList.add("--" + entry.getKey());
            stringList.add(entry.getValue());
        }
        return stringList.toArray(new String[stringList.size()]);
    }
}
