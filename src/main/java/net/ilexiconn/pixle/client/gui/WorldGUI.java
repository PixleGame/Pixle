package net.ilexiconn.pixle.client.gui;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.client.render.RenderingRegistry;
import net.ilexiconn.pixle.client.render.entity.IEntityRenderer;
import net.ilexiconn.pixle.world.World;
import net.ilexiconn.pixle.world.entity.Entity;
import net.ilexiconn.pixle.world.entity.PlayerEntity;
import net.ilexiconn.pixle.world.pixel.Pixel;
import net.ilexiconn.pixle.world.region.Region;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class WorldGUI extends GUI {
    public WorldGUI(PixleClient pixle) {
        super(pixle);
    }

    @Override
    public void render() {
        World world = pixle.getWorld();
        PlayerEntity player = pixle.getPlayer();

        for (Entity entity : world.getEntities()) {
            IEntityRenderer entityRenderer = RenderingRegistry.getEntityRenderer(entity.getClass());
            if (entityRenderer != null) {
                entityRenderer.render(entity, world, (float) pixle.getDelta());
            }
        }

        int regionCount = (Display.getWidth() / 128) + 1;
        int playerRegionX = world.getRegionX(player.posX);

        for (int regionX = 0; regionX < regionCount; regionX++) {
            Region region = world.getRegion((playerRegionX + regionX) - (regionCount / 2));
            for (int y = 0; y < 256; y++) {
                int relativeY = y - player.posY;
                int centerY = (Display.getHeight() / 128) / 2;

                if (relativeY > -centerY && relativeY < centerY) {
                    for (int x = 0; x < 16; x++) {
                        Pixel pixel = region.getPixel(x, y);
                        if (pixel != null) {
                            int hex = pixel.getMaterial().getColor();
                            int r = (hex & 0xFF0000) >> 16;
                            int g = (hex & 0xFF00) >> 8;
                            int b = (hex & 0xFF);
                            GL11.glColor3f(r / 255.0F, g / 255.0F, b / 255.0F);
                        } else {
                            GL11.glColor3f(0.0F, 148.0F / 255.0F, 1.0F);
                        }
                        drawRect((x + (regionX * 16)) * 8, (Display.getHeight() / 2) - (y * 8), 8, 8);
                    }
                }
            }
        }
    }
}
