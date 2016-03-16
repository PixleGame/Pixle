package net.ilexiconn.pixle.client.render;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.client.gl.GLStateManager;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

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

        GLStateManager.end();
    }

    public static void drawOutline(int x, int y, int width, int height, int outlineSize) {
        drawRect(x, y, width, outlineSize);
        drawRect(x + width, y, outlineSize, height);
        drawRect(x, y + height, width + outlineSize, outlineSize);
        drawRect(x, y, outlineSize, height);
    }

    public static void drawVertex(int x, int y, int u, int v, float uMultiplier, float vMultiplier) {
        GL11.glTexCoord2f(u * uMultiplier, v * vMultiplier);
        GL11.glVertex2f(x, y);
    }

    public static void drawCenteredScaledString(int x, int y, String text, float scale) {
        TrueTypeFont font = PixleClient.INSTANCE.getFontRenderer();

        GLStateManager.pushMatrix();
        GLStateManager.enableTexture();
        GLStateManager.scale(scale, scale);

        float drawX = (x / scale) - (font.getWidth(text) / 2);
        float drawY = (y - (font.getHeight(text) / 2)) / scale;

        font.drawString(drawX, drawY, text);

        GLStateManager.popMatrix();
    }

    public static void drawCenteredScaledStringWithShadow(int x, int y, String text, float scale) {
        TrueTypeFont font = PixleClient.INSTANCE.getFontRenderer();

        GLStateManager.pushMatrix();
        GLStateManager.enableTexture();
        GLStateManager.scale(scale, scale);

        float drawX = (x / scale) - (font.getWidth(text) / 2);
        float drawY = (y - (font.getHeight(text) / 2)) / scale;

        font.drawString(drawX + 2, drawY + 2, text, new Color(0x606060));
        font.drawString(drawX, drawY, text);

        GLStateManager.popMatrix();
    }

    public static void drawScaledStringWithShadow(int x, int y, String text, float scale) {
        TrueTypeFont font = PixleClient.INSTANCE.getFontRenderer();

        GLStateManager.pushMatrix();
        GLStateManager.enableTexture();
        GLStateManager.scale(scale, scale);

        float drawX = x / scale;
        float drawY = y / scale;

        font.drawString(drawX + 2, drawY + 2, text, new Color(0x606060));
        font.drawString(drawX, drawY, text);

        GLStateManager.popMatrix();
    }
}
