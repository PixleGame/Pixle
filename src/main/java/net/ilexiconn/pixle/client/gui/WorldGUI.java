package net.ilexiconn.pixle.client.gui;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.client.gl.GLStateManager;
import net.ilexiconn.pixle.client.render.RenderHelper;
import net.ilexiconn.pixle.client.render.RenderingRegistry;
import net.ilexiconn.pixle.client.render.entity.IEntityRenderer;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.level.region.Region;
import org.lwjgl.opengl.Display;

public class WorldGUI extends GUI {
    public WorldGUI(PixleClient pixle) {
        super(pixle);
    }

    @Override
    public void render() {
        Level level = pixle.getLevel();
        PlayerEntity player = pixle.getPlayer();

        if (player != null) {
            int pixelSize = Level.PIXEL_SIZE;
            int pixelsInWidth = (int) Math.ceil(Display.getWidth() / pixelSize);
            int pixelsInHeight = (int) Math.ceil(Display.getHeight() / pixelSize);
            int halfPixelsInHeight = pixelsInHeight / 2;

            int centerX = Display.getWidth() / 2;
            int centerY = Display.getHeight() / 2;

            GLStateManager.setColor(0x0094FF);
            RenderHelper.drawRect(0, 0, Display.getWidth(), (int) (Display.getHeight() - (centerY - ((player.posY + 1) * pixelSize))));

            for (PixelLayer layer : PixelLayer.values()) {
                for (int y = 0; y < Region.REGION_HEIGHT; y++) {
                    int relativeY = (int) player.posY - y;
                    if (Math.abs(relativeY) <= halfPixelsInHeight) {
                        for (int x = (int) (player.posX - (pixelsInWidth / 2)) - 1; x < player.posX + (pixelsInWidth / 2) + 1; x++) {
                            if (level.hasPixel(x, y, layer)) {
                                GLStateManager.setColor(level.getPixel(x, y, layer));
                                RenderHelper.drawRect((int) (centerX - Math.round((player.posX - x) * pixelSize)), Display.getHeight() - (centerY - (int) Math.round((player.posY - y) * pixelSize)), pixelSize, pixelSize);
                            }
                        }
                    }
                }
            }

            for (Entity entity : level.getEntities()) {
                IEntityRenderer entityRenderer = RenderingRegistry.getEntityRenderer(entity.getClass());
                if (entityRenderer != null) {
                    entityRenderer.render(entity, centerX - (int) ((player.posX - entity.posX) * pixelSize), centerY - (int) Math.round((entity.posY - player.posY) * pixelSize), level, (float) pixle.getDelta());
                }
            }
        }
    }
}
