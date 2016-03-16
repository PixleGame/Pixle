package net.ilexiconn.pixle.client.render.entity;

import net.ilexiconn.pixle.client.gl.GLStateManager;
import net.ilexiconn.pixle.client.render.RenderHelper;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.Level;

public class PlayerEntityRenderer implements IEntityRenderer<PlayerEntity> {
    @Override
    public void render(PlayerEntity entity, int x, int y, Level level, float delta) {
        int pixelSize = Level.PIXEL_SIZE;
        GLStateManager.setColor(23.0F / 255.0F, 38.0F / 255.0F, 124.0F / 255.0F);
        RenderHelper.drawRect(x, y, pixelSize, pixelSize);
        GLStateManager.setColor(168.0F / 255.0F, 111.0F / 255.0F, 87.0F / 255.0F);
        RenderHelper.drawRect(x, y - pixelSize, pixelSize, pixelSize);
        GLStateManager.enableTexture();
        RenderHelper.drawCenteredScaledString(x, y - 2, entity.username, 0.4F);
    }
}
