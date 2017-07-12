package org.pixle.client.gui;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class RenderResolution {
    private static final int BASE_WIDTH = 854;
    private static final int BASE_HEIGHT = 480;
    private int width;
    private int height;
    private float scale = 1.0F;

    public RenderResolution() {
        int displayWidth = Display.getWidth();
        int displayHeight = Display.getHeight();

        while (displayWidth / (scale + 1.0F) >= BASE_WIDTH && displayHeight / (scale + 1.0F) >= BASE_HEIGHT) {
            scale++;
        }

        width = (int) Math.ceil(displayWidth / scale);
        height = (int) Math.ceil(displayHeight / scale);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void applyScale() {
        GL11.glScalef(scale, scale, 1.0F); //Shh, don't tell the GLStateManager about these changes! 'They didn't happen!'
    }

    public float getScale() {
        return scale;
    }
}
