package org.pixle.client.render;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.pixle.client.PixleClient;
import org.pixle.client.gl.GLStateManager;

public class RenderHelper {
    public static void drawTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
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

    public static void drawRect(float x, float y, float width, float height) {
        GLStateManager.disableTexture();
        GL11.glRectf(x, y, x + width, y + height);
    }

    public static void drawOutline(float x, float y, float width, float height, float outlineSize) {
        drawRect(x, y, width - outlineSize, outlineSize);
        drawRect(x + width - outlineSize, y, outlineSize, height - outlineSize);
        drawRect(x, y + height - outlineSize, width, outlineSize);
        drawRect(x, y, outlineSize, height - outlineSize);
    }

    public static void drawVertex(float x, float y, float u, float v, float uMultiplier, float vMultiplier) {
        GL11.glTexCoord2f(u * uMultiplier, v * vMultiplier);
        GL11.glVertex2f(x, y);
    }

    public static void drawCenteredScaledString(float x, float y, String text, float scale) {
        TrueTypeFont font = PixleClient.INSTANCE.getFontRenderer();

        GLStateManager.pushMatrix();
        GLStateManager.enableTexture();
        GLStateManager.scale(scale, scale);

        float drawX = (x / scale) - (font.getWidth(text) / 2);
        float drawY = (y - (font.getHeight(text) / 2)) / scale;

        GLStateManager.setColor(0xFFFFFF);
        drawString(drawX, drawY, text);

        GLStateManager.popMatrix();
    }

    public static void drawCenteredScaledStringWithShadow(float x, float y, String text, float scale) {
        TrueTypeFont font = PixleClient.INSTANCE.getFontRenderer();

        GLStateManager.pushMatrix();
        GLStateManager.enableTexture();
        GLStateManager.scale(scale, scale);

        float drawX = (x / scale) - (font.getWidth(text) / 2);
        float drawY = (y / scale) - (font.getHeight(text) / 2);

        GLStateManager.setColor(0x606060);
        drawString(drawX + 1, drawY + 1, text);
        GLStateManager.setColor(0xFFFFFF);
        drawString(drawX, drawY, text);

        GLStateManager.popMatrix();
    }

    public static void drawScaledStringWithShadow(float x, float y, String text, float scale) {
        GLStateManager.pushMatrix();
        GLStateManager.enableTexture();
        GLStateManager.scale(scale, scale);

        float drawX = x / scale;
        float drawY = y / scale;

        GLStateManager.setColor(0x606060);
        drawString(drawX + 1, drawY + 1, text);
        GLStateManager.setColor(0xFFFFFF);
        drawString(drawX, drawY, text);

        GLStateManager.popMatrix();
    }

    public static void drawString(float x, float y, String text) {
        TrueTypeFont font = PixleClient.INSTANCE.getFontRenderer();
        font.drawString(x, y - 5, text, GLStateManager.getColor());
    }
}
