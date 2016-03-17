package org.pixle.client.gui;

import org.lwjgl.opengl.Display;
import org.pixle.client.gl.GLStateManager;

public class RenderResolution {
    private int width;
    private int height;

    private float scale = 1.0F;

    private static final int BASE_WIDTH = 854;
    private static final int BASE_HEIGHT = 480;

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
        GLStateManager.scale(scale, scale);
    }

    public float getScale() {
        return scale;
    }
}
