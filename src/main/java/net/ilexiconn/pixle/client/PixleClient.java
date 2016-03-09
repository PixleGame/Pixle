package net.ilexiconn.pixle.client;

import net.ilexiconn.pixle.client.gui.BaseGUI;
import net.ilexiconn.pixle.client.gui.TestGUI;
import net.ilexiconn.pixle.client.render.PixleRenderHandler;
import net.ilexiconn.pixle.crash.CrashReport;

public class PixleClient {
    public static final int SCREEN_WIDTH = 854;
    public static final int SCREEN_HEIGHT = 480;

    private boolean closeRequested;

    private PixleRenderHandler renderHandler;

    private BaseGUI openGUI;

    public void start() {
        try {
            startTick();
        } catch (Exception e) {
            System.err.println(CrashReport.makeCrashReport(e, "An unexpected error occurred."));
        }
    }

    public void stop() {
        closeRequested = true;
    }

    private void startTick() {
        renderHandler = new PixleRenderHandler(this);
        renderHandler.start();

        openGUI(new TestGUI());

        double delta = 0;
        long previousTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        int ups = 0;
        double nanoUpdates = 1000000000.0 / 60.0;

        while (!isCloseRequested()) {
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

    public void openGUI(BaseGUI gui) {
        openGUI = gui;
    }

    public BaseGUI getOpenGUI() {
        return openGUI;
    }

    public boolean isCloseRequested() {
        return closeRequested;
    }
}
