package net.ilexiconn.pixle.server;

import net.ilexiconn.netconn.server.IServerListener;
import net.ilexiconn.netconn.server.Server;
import net.ilexiconn.pixle.crash.CrashReport;
import net.ilexiconn.pixle.event.bus.EventBus;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.ServerLevel;
import net.ilexiconn.pixle.network.PixleNetworkManager;

import java.io.IOException;
import java.net.Socket;

public class PixleServer implements IServerListener {
    private boolean closeRequested;
    private Server server;
    private ServerLevel level;

    public static PixleServer INSTANCE;
    public static final EventBus EVENT_BUS = new EventBus();

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
        PixleNetworkManager.init();
        server = new Server(port);
        server.addListener(this);
        level = new ServerLevel();
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

        new Thread(() -> {
            while (server.isRunning()) {
                try {
                    server.waitForConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (server.isRunning()) {
                server.listen();
            }
        }).start();

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
    public void onClientConnected(Server server, Socket client) {
    }

    @Override
    public void onClientDisconnected(Server server, Socket client) {
        String username = PixleNetworkManager.clients.get(client);
        PixleNetworkManager.clients.remove(client);
        if (username != null) {
            System.out.println(username + " has disconnected!");
        }
        level.removeEntity(level.getPlayerByUsername(username));
    }
}
