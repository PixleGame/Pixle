package net.ilexiconn.pixle.client.render;

import net.ilexiconn.pixle.client.PixleClient;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class PixleRenderHandler extends Thread {
    private PixleClient client;

    private int fps;

    public PixleRenderHandler(PixleClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Display.setDisplayMode(new DisplayMode(PixleClient.SCREEN_WIDTH, PixleClient.SCREEN_HEIGHT));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        long timer = System.currentTimeMillis();

        while (!Display.isCloseRequested() && !client.isCloseRequested()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glLoadIdentity();

            fps++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                Display.setTitle("Pixle - FPS: " + fps);
                fps = 0;
            }

            Display.update();
        }
    }
}
