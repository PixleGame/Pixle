package net.ilexiconn.pixle.client;

import net.ilexiconn.pixle.client.render.PixleRenderHandler;

public class PixleClient {
    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 800;

    private boolean closeRequested;

    private PixleRenderHandler renderHandler;

    public void start() {
        startTick();
    }

    public void stop() {
        closeRequested = true;
    }

    private void startTick() {
        renderHandler = new PixleRenderHandler(this);
        renderHandler.start();

        double delta = 0;
        long previousTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        int ups = 0;
        double nanoUpdates = 1000000000.0 / 60.0;

        while (!closeRequested) {
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
}
