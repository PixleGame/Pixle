package org.pixle.client.gui;

import org.lwjgl.opengl.Display;
import org.pixle.client.PixleClient;
import org.pixle.client.gl.GLStateManager;
import org.pixle.client.gui.component.ButtonComponent;
import org.pixle.client.gui.component.TextBoxComponent;
import org.pixle.client.render.RenderHelper;

public class SelectServerGUI extends GUI {
    private TextBoxComponent ipTextBox;

    @Override
    public void updateComponents() {
        int width = Display.getWidth();
        int height = Display.getHeight();
        addComponent(new ButtonComponent(width / 2 - 225, (height - (height / 8)) - 20, 200, 40, "Back", button -> {
            PixleClient client = PixleClient.INSTANCE;
            SelectServerGUI.this.close();
            client.openGUI(new MainMenuGUI());
        }));
        addComponent(new ButtonComponent(width / 2 + 25, (height - (height / 8)) - 20, 200, 40, "Connect", button -> {
            PixleClient client = PixleClient.INSTANCE;
            SelectServerGUI.this.close();
            client.openGUI(new LevelGUI());
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
        addComponent(ipTextBox);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        int width = Display.getWidth();
        int height = Display.getHeight();
        GLStateManager.setColor(0x0094FF);
        RenderHelper.drawRect(0, 0, width, height);
        RenderHelper.drawCenteredScaledStringWithShadow(width / 2, 40, "Join Server", 2.0F);
        RenderHelper.drawCenteredScaledStringWithShadow(width / 2, height / 2 - 50, "IP", 1.0F);
        super.render(mouseX, mouseY);
    }
}
