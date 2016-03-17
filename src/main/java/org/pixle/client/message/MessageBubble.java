package org.pixle.client.message;

import org.pixle.client.PixleClient;
import org.pixle.client.gl.GLStateManager;
import org.pixle.client.gui.GUI;
import org.pixle.client.gui.LevelGUI;
import org.pixle.client.render.RenderHelper;
import org.pixle.entity.PlayerEntity;
import org.pixle.level.Level;

public class MessageBubble {
    private String message;
    private int tick;
    private int x;
    private int y;

    public MessageBubble(String message, int x, int y) {
        this.message = message;
        this.x = x;
        this.y = y;
    }

    public void tick() {
        tick++;
        if (tick % 120 == 1) {
            y++;
        }
        if (tick > 1200) {
            LevelGUI levelGUI = null;
            for (GUI gui : PixleClient.INSTANCE.getOpenGUIs()) {
                if (gui instanceof LevelGUI) {
                    levelGUI = (LevelGUI) gui;
                }
            }
            levelGUI.bubbleList.remove(this);
        }
    }

    public void render(int centerX, int centerY, PlayerEntity player) {
        int pixelSize = Level.PIXEL_SIZE;
        float x = (float) (centerX - (player.posX - this.x) * pixelSize);
        float y = (float) (centerY - (this.y - player.posY) * pixelSize);
        GLStateManager.enableBlend();
        RenderHelper.drawCenteredScaledStringWithShadow(x, y, message, 1.0F);
    }
}
