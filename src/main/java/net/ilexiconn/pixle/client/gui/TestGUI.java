package net.ilexiconn.pixle.client.gui;

import org.lwjgl.opengl.GL11;

public class TestGUI extends BaseGUI {
    @Override
    public void render() {
        GL11.glColor3f(1.0F, 0.0F, 0.0F);
        drawRect(50, 50, 50, 50);

        GL11.glColor3f(1.0F, 1.0F, 0.0F);
        drawRect(125, 50, 25, 25);
    }
}
