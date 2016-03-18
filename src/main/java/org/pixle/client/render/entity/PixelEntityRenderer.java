package org.pixle.client.render.entity;

import org.pixle.client.gl.GLStateManager;
import org.pixle.client.render.RenderHelper;
import org.pixle.entity.PixelEntity;
import org.pixle.level.Level;
import org.pixle.pixel.Pixel;

public class PixelEntityRenderer implements IEntityRenderer<PixelEntity> {
    @Override
    public void render(PixelEntity entity, float x, float y, Level level, float delta) {
        int pixelSize = Level.PIXEL_SIZE;
        GLStateManager.pushMatrix();
        float scale = 0.75F;
        GLStateManager.scale(scale, scale);
        Pixel pixel = entity.pixel.getPixel();
        GLStateManager.setColor(pixel.getRed(), pixel.getGreen(), pixel.getBlue());
        RenderHelper.drawRect(x / scale - (pixelSize / 2), y / scale + (pixelSize / 2), pixelSize, pixelSize);
        GLStateManager.popMatrix();
    }
}
