import net.ilexiconn.pixle.util.SystemUtils;

import java.util.Map;

public class ServerDevStart extends DevStart {
    public static void main(String[] args) throws ReflectiveOperationException {
        new ServerDevStart().start(args);
    }

    @Override
    public void applyDefaults(Map<String, String> properties) {
        applyDefault(properties, "port", 25565);
        applyDefault(properties, "gamefolder", SystemUtils.getGameFolder().getAbsolutePath());
    }

    @Override
    public String getStartupClassName() {
        return "net.ilexiconn.pixle.server.PixleServerStartup";
    }
}
