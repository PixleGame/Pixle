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
import org.pixle.network.SetPixelPacket;
import org.pixle.pixel.Pixel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
                                int color = pixel.getColor();
                                int r = ((color & 0xFF0000) >> 16);
                                int g = ((color & 0xFF00) >> 8);
                                int b = (color & 0xFF);
                                int offset = new Random((x - y) * y).nextInt(10) - 5;
                                GLStateManager.setColor((r + offset) / 255.0F, (g + offset) / 255.0F, (b + offset) / 255.0F);
                                RenderHelper.drawRect((float) (centerX - (player.posX - x) * pixelSize), (float) (height - (centerY - (player.posY - y) * pixelSize)), pixelSize, pixelSize);
                            }
                        }
                    }
                }
            }

            int selectionX = pixle.getSelectionX(mouseX);
            int selectionY = pixle.getSelectionY(mouseY);

            int distX = (int) (selectionX - player.posX);
            int distY = (int) (selectionY - player.posY);
            double dist = Math.sqrt(distX * distX + distY * distY);
            if (dist < 10) {
                for (PixelLayer layer : PixelLayer.values()) {
                    if (level.hasPixel(selectionX, selectionY, layer)) {
                        GLStateManager.setColor(0);
                        RenderHelper.drawOutline((float) ((centerX - (player.posX - selectionX) * pixelSize)), (float) (height - (centerY - (player.posY - selectionY) * pixelSize)), pixelSize, pixelSize, 1);
                        break;
                   }
                }
            }

            EventBus eventBus = EventBus.get();

            for (Entity entity : level.getEntities()) {
                IEntityRenderer entityRenderer = RenderingRegistry.getEntityRenderer(entity.getClass());
                if (entityRenderer != null) {
                    if (eventBus.post(new RenderEntityEvent.Pre(pixle, entity))) {
                        entityRenderer.render(entity, (float) (centerX - (player.posX - entity.posX) * pixelSize), (float) (centerY - (entity.posY - player.posY) * pixelSize), level, (float) pixle.getDelta());
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
        new ArrayList<>(bubbleList).forEach(MessageBubble::tick);
    }

    @Override
    public void keyTyped(char c, int keyCode) {
        if (keyCode == Keyboard.KEY_RETURN) {
            PixleClient.INSTANCE.getClient().sendTCP(new SendMessagePacket(PixleClient.INSTANCE.getPlayer(), "Test!"));
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        super.mouseClicked(mouseX, mouseY);

        PixleClient pixle = PixleClient.INSTANCE;

        Level level = pixle.getLevel();
        PlayerEntity player = pixle.getPlayer();

        if (player != null) {
            int selectionX = pixle.getSelectionX(mouseX);
            int selectionY = pixle.getSelectionY(mouseY);

            int distX = (int) (selectionX - player.posX);
            int distY = (int) (selectionY - player.posY);
            double dist = Math.sqrt(distX * distX + distY * distY);

            if (dist < PlayerEntity.REACH_DISTANCE) {
                for (PixelLayer layer : PixelLayer.values()) {
                    if (level.hasPixel(selectionX, selectionY, layer)) {
                        player.setPixel(Pixel.AIR, selectionX, selectionY, layer);
                        break;
                    }
                }
            }
        }
    }
}
