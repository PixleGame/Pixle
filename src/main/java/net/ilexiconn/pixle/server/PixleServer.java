package net.ilexiconn.pixle.server;

import net.ilexiconn.netconn.server.Server;
import net.ilexiconn.pixle.crash.CrashReport;
import net.ilexiconn.pixle.event.bus.EventBus;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.ServerLevel;
import net.ilexiconn.pixle.network.PixleNetworkManager;

import java.io.IOException;

public class PixleServer {
    private boolean closeRequested;
    private Server server;
    private Level level;

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
        server.addListener(new PixleNetworkManager());
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
                System.out.println("UPS: " + ups);

                timer += 1000;
                ups = 0;
            }
        }
    }

    private void tick() {

    }

    public boolean isCloseRequested() {
        return closeRequested;
    }

    public Level getLevel() {
        return level;
    }
}
