package org.pixle.client.message;

import org.pixle.client.render.RenderHelper;

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
            y--;
        }
    }

    public void render() {
        RenderHelper.drawCenteredScaledStringWithShadow(x, y, message, 1.0F);
    }
}
