package org.pixle.client.render.entity;

import org.pixle.client.gl.GLStateManager;
import org.pixle.client.render.RenderHelper;
import org.pixle.entity.PixelEntity;
import org.pixle.level.Level;

public class PixelEntityRenderer implements IEntityRenderer<PixelEntity> {
    @Override
    public void render(PixelEntity entity, float x, float y, Level level, float delta) {
        int pixelSize = Level.PIXEL_SIZE;
        GLStateManager.pushMatrix();
        GLStateManager.scale(0.5F, 0.5F);
        GLStateManager.setColor(entity.pixel.getPixel().getColor());
        RenderHelper.drawRect(x / 0.5F - (pixelSize / 2), (y + 4) / 0.5F, pixelSize, pixelSize);
        GLStateManager.popMatrix();
    }
}
