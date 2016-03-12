import net.ilexiconn.pixle.util.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ClientDevStart extends DevStart {
    public static void main(String[] args) throws ReflectiveOperationException {
        new ClientDevStart().start(args);
    }

    @Override
    public void preInit(Map<String, String> properties) {
        File gameFolder = new File(properties.get("gamefolder"));
        try {
            LWJGLSetup.load(new File(gameFolder, "natives"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void applyDefaults(Map<String, String> properties) {
        applyDefault(properties, "username", "Player" + (int) (Math.random() * 1000L));
        applyDefault(properties, "gamefolder", SystemUtils.getGameFolder().getAbsolutePath());
    }

    @Override
    public String getStartupClassName() {
        return "net.ilexiconn.pixle.client.PixleClientStartup";
    }
}
