package net.ilexiconn.pixle.server;

import net.ilexiconn.netconn.server.Server;
import net.ilexiconn.pixle.Materials;
import net.ilexiconn.pixle.crash.CrashReport;

public class PixleServer {
    private boolean closeRequested;
    private Server server;

    public void start(int port) {
        try {
            server = new Server(port);
            Materials.init();
            startTick();
        } catch (Exception e) {
            System.err.println(CrashReport.makeCrashReport(e, "An unexpected error occurred."));
        }
    }

    public void stop() {
        closeRequested = true;
    }

    private void startTick() {
        double delta = 0;
        long previousTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        int ups = 0;

        while (!isCloseRequested()) {
            long currentTime = System.nanoTime();
            delta += (currentTime - previousTime) / 10000000.0;
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
}
