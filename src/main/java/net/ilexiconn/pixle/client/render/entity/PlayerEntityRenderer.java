package net.ilexiconn.pixle.client.render.entity;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.client.gl.GLStateManager;
import net.ilexiconn.pixle.client.render.RenderHelper;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.Level;
import org.newdawn.slick.TrueTypeFont;

public class PlayerEntityRenderer implements IEntityRenderer<PlayerEntity> {
    @Override
    public void render(PlayerEntity entity, int x, int y, Level level, float delta) {
        GLStateManager.pushMatrix();
        int pixelSize = Level.PIXEL_SIZE;
        GLStateManager.setColor(23.0F / 255.0F, 38.0F / 255.0F, 124.0F / 255.0F);
        RenderHelper.drawRect(x, y, pixelSize, pixelSize);
        GLStateManager.setColor(168.0F / 255.0F, 111.0F / 255.0F, 87.0F / 255.0F);
        RenderHelper.drawRect(x, y - pixelSize, pixelSize, pixelSize);
        GLStateManager.enableTexture();
        TrueTypeFont font = PixleClient.INSTANCE.getFontRenderer();
        float scale = 0.4F;
        GLStateManager.scale(scale, scale);
        font.drawString(((x - font.getWidth(entity.username) / 2) + 35) / scale, ((y - 32) + 10) / scale, entity.username);
        GLStateManager.popMatrix();
    }
}
