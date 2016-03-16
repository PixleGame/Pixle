package org.pixle.client.gui;

import org.lwjgl.opengl.Display;
import org.pixle.client.PixleClient;
import org.pixle.client.event.RenderEntityEvent;
import org.pixle.client.gl.GLStateManager;
import org.pixle.client.render.RenderHelper;
import org.pixle.client.render.RenderingRegistry;
import org.pixle.client.render.entity.IEntityRenderer;
import org.pixle.entity.Entity;
import org.pixle.entity.PlayerEntity;
import org.pixle.event.bus.EventBus;
import org.pixle.level.Level;
import org.pixle.level.PixelLayer;

public class WorldGUI extends GUI {
    @Override
    public void updateComponents() {
    }

    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);

        PixleClient pixle = PixleClient.INSTANCE;

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
                for (int y = 0; y < Level.LEVEL_HEIGHT; y++) {
                    int relativeY = (int) player.posY - y;
                    if (Math.abs(relativeY) <= halfPixelsInHeight) {
                        for (int x = (int) (player.posX - (pixelsInWidth / 2)) - 1; x < player.posX + (pixelsInWidth / 2) + 1; x++) {
                            if (level.hasPixel(x, y, layer)) {
                                GLStateManager.setColor(level.getPixel(x, y, layer).getColor());
                                RenderHelper.drawRect((int) (centerX - Math.round((player.posX - x) * pixelSize)), Display.getHeight() - (centerY - (int) Math.round((player.posY - y) * pixelSize)), pixelSize, pixelSize);
                            }
                        }
                    }
                }
            }

            for (Entity entity : level.getEntities()) {
                IEntityRenderer entityRenderer = RenderingRegistry.getEntityRenderer(entity.getClass());
                if (entityRenderer != null) {
                    if (EventBus.get().post(new RenderEntityEvent.Pre(pixle, entity))) {
                        entityRenderer.render(entity, centerX - (int) ((player.posX - entity.posX) * pixelSize), centerY - (int) Math.round((entity.posY - player.posY) * pixelSize), level, (float) pixle.getDelta());
                    }
                    EventBus.get().post(new RenderEntityEvent.Post(pixle, entity));
                }
            }
        }
    }
}
