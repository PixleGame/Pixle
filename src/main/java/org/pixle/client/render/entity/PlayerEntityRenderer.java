package org.pixle.client.render.entity;

import org.pixle.client.gl.GLStateManager;
import org.pixle.client.render.RenderHelper;
import org.pixle.entity.PlayerEntity;
import org.pixle.level.Level;

public class PlayerEntityRenderer implements IEntityRenderer<PlayerEntity> {
    @Override
    public void render(PlayerEntity entity, float x, float y, Level level, float delta) {
        int pixelSize = Level.PIXEL_SIZE;
        GLStateManager.setColor(23.0F / 255.0F, 38.0F / 255.0F, 124.0F / 255.0F);
        RenderHelper.drawRect(x, y, pixelSize, pixelSize);
        GLStateManager.setColor(168.0F / 255.0F, 111.0F / 255.0F, 87.0F / 255.0F);
        RenderHelper.drawRect(x, y - pixelSize, pixelSize, pixelSize);
        GLStateManager.enableTexture();
        RenderHelper.drawCenteredScaledStringWithShadow(x, y - 11, entity.username, 1.0F);
    }
}
