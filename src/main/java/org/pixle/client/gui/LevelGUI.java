package org.pixle.client.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.pixle.client.PixleClient;
import org.pixle.client.event.RenderEntityEvent;
import org.pixle.client.gl.GLStateManager;
import org.pixle.client.message.MessageBubble;
import org.pixle.client.render.RenderHelper;
import org.pixle.client.render.RenderingRegistry;
import org.pixle.client.render.entity.IEntityRenderer;
import org.pixle.entity.Entity;
import org.pixle.entity.PlayerEntity;
import org.pixle.event.bus.EventBus;
import org.pixle.level.Level;
import org.pixle.level.PixelLayer;
import org.pixle.level.region.Region;
import org.pixle.network.SendMessagePacket;
import org.pixle.pixel.Pixel;

import java.util.ArrayList;
import java.util.List;

public class LevelGUI extends GUI {
    public List<MessageBubble> bubbleList = new ArrayList<>();

    @Override
    public void updateComponents(RenderResolution renderResolution) {
    }

    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);

        PixleClient pixle = PixleClient.INSTANCE;

        Level level = pixle.getLevel();
        PlayerEntity player = pixle.getPlayer();

        RenderResolution renderResolution = pixle.getRenderResolution();
        int width = renderResolution.getWidth();
        int height = renderResolution.getHeight();

        if (player != null) {
            int pixelSize = Level.PIXEL_SIZE;
            int pixelsInWidth = (int) Math.ceil(width / pixelSize);
            int pixelsInHeight = (int) Math.ceil(height / pixelSize);
            int halfPixelsInHeight = pixelsInHeight / 2;

            int centerX = width / 2;
            int centerY = height / 2;

            GLStateManager.setColor(0x0090F7);
            RenderHelper.drawRect(0, 0, Display.getWidth(), (int) (Display.getHeight() - (centerY - ((player.posY + 1) * pixelSize))));

            for (PixelLayer layer : PixelLayer.values()) {
                for (int y = (int) (player.posY - halfPixelsInHeight) - 1; y < Math.min(Level.LEVEL_HEIGHT, player.posY + halfPixelsInHeight + 2); y++) {
                    for (int x = (int) (player.posX - (pixelsInWidth / 2)) - 1; x < player.posX + (pixelsInWidth / 2) + 1; x++) {
                        Region region = level.getRegionForPixel(x, y);
                        if (!region.isEmpty(layer)) {
                            Pixel pixel = level.getPixel(x, y, layer);
                            if (pixel != Pixel.AIR) {
                                GLStateManager.setColor(pixel.getColor());
                                RenderHelper.drawRect((int) (centerX - Math.round((player.posX - x) * pixelSize)), height - (centerY - (int) Math.round((player.posY - y) * pixelSize)), pixelSize, pixelSize);
                            }
                        }
                    }
                }
            }

            EventBus eventBus = EventBus.get();

            for (Entity entity : level.getEntities()) {
                IEntityRenderer entityRenderer = RenderingRegistry.getEntityRenderer(entity.getClass());
                if (entityRenderer != null) {
                    if (eventBus.post(new RenderEntityEvent.Pre(pixle, entity))) {
                        entityRenderer.render(entity, centerX - (int) ((player.posX - entity.posX) * pixelSize), centerY - (int) Math.round((entity.posY - player.posY) * pixelSize), level, (float) pixle.getDelta());
                    }
                    eventBus.post(new RenderEntityEvent.Post(pixle, entity));
                }
            }

            for (MessageBubble bubble : new ArrayList<>(bubbleList)) {
                bubble.render(centerX, centerY, player);
            }
        }
    }

    @Override
    public void tick() {
        bubbleList.forEach(MessageBubble::tick);
    }

    @Override
    public void keyTyped(char c, int keyCode) {
        if (keyCode == Keyboard.KEY_RETURN) {
            PixleClient.INSTANCE.getClient().sendTCP(new SendMessagePacket(PixleClient.INSTANCE.getPlayer(), "Test!"));
        }
    }
}
