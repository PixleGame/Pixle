package net.ilexiconn.pixle.client.gui;

import net.ilexiconn.pixle.client.PixleClient;
import org.lwjgl.opengl.GL11;

public class TestGUI extends BaseGUI {
    public TestGUI(PixleClient pixle) {
        super(pixle);
    }

    @Override
    public void render() {
        GL11.glColor3f(1.0F, 0.0F, 0.0F);
        drawRect(50, 50, 50, 50);

        GL11.glColor3f(1.0F, 1.0F, 0.0F);
        drawRect(125, 50, 25, 25);

        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        pixle.getTextureManager().bindTexture("/textures/test.png");
        drawTexture(200, 50, 0, 0, 128, 128, 128, 128);
    }
}
