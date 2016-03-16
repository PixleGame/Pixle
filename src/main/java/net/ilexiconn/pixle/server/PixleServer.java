package net.ilexiconn.pixle.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import net.ilexiconn.pixle.event.bus.EventBus;
import net.ilexiconn.pixle.event.event.PixleInitializeEvent;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.ServerLevel;
import net.ilexiconn.pixle.network.PixleNetworkManager;
import net.ilexiconn.pixle.network.PixlePacket;
import net.ilexiconn.pixle.plugin.PluginJson;
import net.ilexiconn.pixle.util.CrashReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PixleServer extends Listener {
    public List<PluginJson> pluginList = new ArrayList<>();

    private boolean closeRequested;
    private Server server;
    private ServerLevel level;

    public static PixleServer INSTANCE;

    public void start(int port) {
        try {
            INSTANCE = this;
            init(port);
            startTick();
        } catch (Exception e) {
            System.err.println(CrashReport.makeCrashReport(e, e.getLocalizedMessage()));
        }
    }

    private void init(int port) throws IOException {
        server = new Server(32767, 32767);
        server.start();
        server.bind(port);
        server.addListener(this);
        PixleNetworkManager.init(server);
        level = new ServerLevel();

        EventBus.get().post(new PixleInitializeEvent());
    }

    public void stop() {
        server.stop();
        closeRequested = true;
    }

    private void startTick() {
        double delta = 0;
        long previousTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        int ups = 0;
        double nanoUpdates = 1000000000.0 / 60.0;

        while (!isCloseRequested() && !closeRequested) {
            long currentTime = System.nanoTime();
            delta += (currentTime - previousTime) / nanoUpdates;
            previousTime = currentTime;

            while (delta >= 1) {
                tick();

                delta--;
                ups++;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                ups = 0;
            }
        }
    }

    private void tick() {
        level.update();
    }

    public boolean isCloseRequested() {
        return closeRequested;
    }

    public Level getLevel() {
        return level;
    }

    public Server getServer() {
        return server;
    }

    @Override
    public void disconnected(Connection connection) {
        String username = PixleNetworkManager.clients.get(connection);
        PixleNetworkManager.clients.remove(connection);
        if (username != null) {
            Log.info("Server", username + " has disconnected!");
        }
        level.removeEntity(level.getPlayerByUsername(username));
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof PixlePacket) {
            PixlePacket packet = (PixlePacket) object;
            packet.handleServer(connection);
        }
    }
}
