package net.ilexiconn.pixle.client.render.entity;

import net.ilexiconn.pixle.client.render.RenderHelper;
import net.ilexiconn.pixle.world.World;
import net.ilexiconn.pixle.world.entity.PlayerEntity;
import org.lwjgl.opengl.GL11;

public class PlayerEntityRenderer implements IEntityRenderer<PlayerEntity> {
    @Override
    public void render(PlayerEntity entity, int x, int y, World world, float delta) {
        int pixelSize = World.PIXEL_SIZE;
        GL11.glColor3f(23.0F / 255.0F, 38.0F / 255.0F, 124.0F / 255.0F);
        RenderHelper.drawRect(x, y, pixelSize, pixelSize);
        GL11.glColor3f(168.0F / 255.0F, 111.0F / 255.0F, 87.0F / 255.0F);
        RenderHelper.drawRect(x, y - pixelSize, pixelSize, pixelSize);
    }
}
