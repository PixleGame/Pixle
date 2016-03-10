package net.ilexiconn.pixle.client.render;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.client.gui.BaseGUI;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
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
            Keyboard.create();
            Mouse.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        long timer = System.currentTimeMillis();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, PixleClient.SCREEN_WIDTH, 0, PixleClient.SCREEN_HEIGHT, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        while (!Display.isCloseRequested() && !client.isCloseRequested()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glPushMatrix();

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            client.getWorldGUI().render();

            BaseGUI openGUI = client.getOpenGUI();

            if (openGUI != null) {
                openGUI.render();
            }

            fps++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                Display.setTitle("Pixle - FPS: " + fps);
                fps = 0;
            }

            GL11.glPopMatrix();

            Display.update();
        }

        client.stop();
    }
}
