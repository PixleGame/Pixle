package org.pixle.client.message;

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
    }

    public void render(int centerX, int centerY, PlayerEntity player) {
        int pixelSize = Level.PIXEL_SIZE;
        RenderHelper.drawCenteredScaledStringWithShadow(centerX - (int) ((player.posX - x) * pixelSize), centerY - (int) ((y - player.posY) * pixelSize - (tick / 4)), message, 1.0F);
    }
}
