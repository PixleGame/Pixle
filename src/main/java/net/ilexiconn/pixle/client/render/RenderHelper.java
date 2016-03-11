package net.ilexiconn.pixle.client.render;

import net.ilexiconn.pixle.client.gl.GLStateManager;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class RenderHelper {
    public static void drawTexture(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight) {
        float uMultiplier = 1.0F / textureWidth;
        float vMultiplier = 1.0F / textureHeight;

        GLStateManager.enableTexture();
        GLStateManager.startDrawingQuads();

        drawVertex(x, y + height, u, v + height, uMultiplier, vMultiplier);
        drawVertex(x + width, y + height, u + width, v + height, uMultiplier, vMultiplier);
        drawVertex(x + width, y, u + width, v, uMultiplier, vMultiplier);
        drawVertex(x, y, u, v, uMultiplier, vMultiplier);

        GLStateManager.end();
    }

    public static void drawRect(int x, int y, int width, int height) {
        float uMultiplier = 1.0F / width;
        float vMultiplier = 1.0F / height;

        GLStateManager.disableTexture();
        GLStateManager.startDrawingQuads();

        drawVertex(x, y + height, 0, height, uMultiplier, vMultiplier);
        drawVertex(x + width, y + height, width, height, uMultiplier, vMultiplier);
        drawVertex(x + width, y, width, 0, uMultiplier, vMultiplier);
        drawVertex(x, y, 0, 0, uMultiplier, vMultiplier);

        GL11.glEnd();
    }

    public static void drawVertex(int x, int y, int u, int v, float uMultiplier, float vMultiplier) {
        GL11.glTexCoord2f(u * uMultiplier, v * vMultiplier);
        GL11.glVertex2f(x, Display.getHeight() - y);
    }
}
