package org.pixle.client.gui;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.TrueTypeFont;
import org.pixle.client.PixleClient;
import org.pixle.client.gl.GLStateManager;
import org.pixle.client.gui.component.ButtonComponent;
import org.pixle.client.render.RenderHelper;

public class MainMenuGUI extends GUI {
    @Override
    public void updateComponents() {
        int width = Display.getWidth();
        int height = Display.getHeight();
        addComponent(new ButtonComponent((width / 2) - 200, (height / 3) - 20, 400, 40, "Play Singleplayer", button -> {
            PixleClient client = PixleClient.INSTANCE;
            MainMenuGUI.this.close();
            client.openGUI(new WorldGUI());
            client.startGame();
        }));
        addComponent(new ButtonComponent((width / 2) - 200, (height / 3) + 40, 400, 40, "Play Multiplayer", button -> {
            PixleClient client = PixleClient.INSTANCE;
            MainMenuGUI.this.close();
            client.openGUI(new SelectServerGUI());
        }));
    }

    @Override
    public void render(int mouseX, int mouseY) {
        int width = Display.getWidth();
        int height = Display.getHeight();

        int centerX = width / 2;

        GLStateManager.setColor(0x0094FF);
        RenderHelper.drawRect(0, 0, width, height);

        RenderHelper.drawCenteredScaledStringWithShadow(centerX, height / 10, "Pixle", 2.0F);

        String createdByString = "Created by iLexiconn and gegy1000";

        TrueTypeFont font = PixleClient.INSTANCE.getFontRenderer();

        RenderHelper.drawScaledStringWithShadow(width - font.getWidth(createdByString) - 10, height - font.getHeight(createdByString) - 5, createdByString, 1.0F);

        super.render(mouseX, mouseY);
    }
}
