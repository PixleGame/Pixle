package net.ilexiconn.pixle.server;

import net.ilexiconn.netconn.server.Server;
import net.ilexiconn.pixle.crash.CrashReport;

import java.io.IOException;

public class PixleServer {
    private boolean closeRequested;
    private Server server;

    public void start(int port) {
        try {
            server = new Server(port);
            startTick();
        } catch (Exception e) {
            System.err.println(CrashReport.makeCrashReport(e, e.getLocalizedMessage()));
        }
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

        new Thread() {
            public void run() {
                try {
                    server.waitForConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        while (!isCloseRequested()) {
            long currentTime = System.nanoTime();
            delta += (currentTime - previousTime) / nanoUpdates;
            previousTime = currentTime;

            server.listen();

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
}
