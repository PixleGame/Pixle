package org.pixle.client.message;

import org.pixle.client.PixleClient;
import org.pixle.client.gl.GLStateManager;
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
        if (tick % 60 == 1) {
            y++;
        }
    }

    public void render(int centerX, int centerY, PlayerEntity player) {
        int pixelSize = Level.PIXEL_SIZE;
        int x = centerX - (int) ((player.posX - this.x) * pixelSize);
        int y = centerY - (int) ((this.y - player.posY) * pixelSize);
        GLStateManager.enableTexture();
        PixleClient.INSTANCE.getTextureManager().bindTexture("/textures/gui/messageBubble.png");
        RenderHelper.drawTexture(x, y, 0, 0, 3, 3, 16, 16);
        RenderHelper.drawCenteredScaledStringWithShadow(x, y, message, 1.0F);
    }
}
