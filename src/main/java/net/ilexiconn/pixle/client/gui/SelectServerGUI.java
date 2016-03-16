package net.ilexiconn.pixle.client.gui;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.client.gl.GLStateManager;
import net.ilexiconn.pixle.client.gui.component.ButtonComponent;
import net.ilexiconn.pixle.client.gui.component.TextBoxComponent;
import net.ilexiconn.pixle.client.render.RenderHelper;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class SelectServerGUI extends GUI {
    private TextBoxComponent ipTextBox;

    @Override
    public void updateComponents() {
        int width = Display.getWidth();
        int height = Display.getHeight();
        addComponenent(new ButtonComponent(width / 2 + 25, (height - (height / 8)) - 20, 200, 40, "Back", button -> {
            PixleClient client = PixleClient.INSTANCE;
            SelectServerGUI.this.close();
            client.openGUI(new MainMenuGUI());
        }));
        addComponenent(new ButtonComponent(width / 2 - 225, (height - (height / 8)) - 20, 200, 40, "Connect", button -> {
            PixleClient client = PixleClient.INSTANCE;
            SelectServerGUI.this.close();
            client.openGUI(new WorldGUI());
            String ip = ipTextBox.getText();
            String host = ip;
            int port = 25565;
            if (ip.contains(":")) {
                String[] split = ip.split(":");
                host = split[0];
                port = Integer.parseInt(split[1]);
            }
            client.setServer(host, port);
            client.startGame();
        }));

        ipTextBox = new TextBoxComponent((width / 2) - 200, height / 2 - 20, 400, 40, ipTextBox != null ? ipTextBox.getText() : "localhost");
        addComponenent(ipTextBox);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        int width = Display.getWidth();
        int height = Display.getHeight();
        GLStateManager.setColor(0x0094FF);
        RenderHelper.drawRect(0, 0, width, height);
        drawCenteredScaledStringWithShadow(width / 2, 40, "Join Server", 2.0F);
        drawCenteredScaledStringWithShadow(width / 2, height / 2 - 50, "IP", 1.0F);
        super.render(mouseX, mouseY);
    }

    private void drawCenteredScaledStringWithShadow(int x, int y, String text, float scale) {
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
}
