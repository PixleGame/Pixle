package org.pixle.client.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.pixle.client.PixleClient;
import org.pixle.client.config.ClientConfig;
import org.pixle.client.event.RenderEntityEvent;
import org.pixle.client.gl.GLStateManager;
import org.pixle.client.message.MessageBubble;
import org.pixle.client.render.RenderHelper;
import org.pixle.client.render.RenderingRegistry;
import org.pixle.client.render.entity.IEntityRenderer;
import org.pixle.entity.Entity;
import org.pixle.entity.PlayerEntity;
import org.pixle.entity.inventory.Inventory;
import org.pixle.event.bus.EventBus;
import org.pixle.level.Level;
import org.pixle.level.PixelLayer;
import org.pixle.level.region.Region;
import org.pixle.network.SendMessagePacket;
import org.pixle.pixel.Pixel;
import org.pixle.pixel.PixelStack;
import org.pixle.util.PixelBounds;

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

            boolean colorNoise = pixle.config.pixelColorNoise;

            PixelLayer[] renderLayers = PixelLayer.values();
            for (int y = Math.max(0, (int) (player.posY - halfPixelsInHeight) - 1); y < Math.min(Level.LEVEL_HEIGHT, player.posY + halfPixelsInHeight + 2); y++) {
                for (int x = (int) (player.posX - (pixelsInWidth / 2)) - 1; x < player.posX + (pixelsInWidth / 2) + 1; x++) {
                    Region region = level.getRegionForPixel(x, y);
                    for (PixelLayer layer : renderLayers) {
                        float colorOffset = layer.getColorOffset() / 255.0F;
                        if (!region.isEmpty(layer)) {
                            Pixel pixel = level.getPixel(x, y, layer);
                            if (pixel != Pixel.AIR) {
                                float offset = (colorNoise ? ((new Random((x - y) * y).nextInt(10) - 5)) / 255.0F : 0.0F) + colorOffset;
                                GLStateManager.setColor(pixel.getRed() + offset, pixel.getGreen() + offset, pixel.getBlue() + offset);
                                RenderHelper.drawRect((float) (centerX - (player.posX - x) * pixelSize), (float) (height - (centerY - (player.posY - y) * pixelSize)), pixelSize, pixelSize);
                                break;
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
            if (dist < PlayerEntity.REACH_DISTANCE) {
                for (PixelLayer layer : PixelLayer.values()) {
                    if (canSelect(level, selectionX, selectionY, layer)) {
                        GLStateManager.setColor(0);
                        RenderHelper.drawOutline((float) ((centerX - (player.posX - selectionX) * pixelSize)), (float) (height - (centerY - (player.posY - selectionY) * pixelSize)), pixelSize, pixelSize, 1);
                        break;
                    }
                }
            }

            Inventory inventory = player.getInventory();

            for (int index = 0; index < 9; index++) {
                PixelStack stack = inventory.getPixelStack(index);
                float color = index % 2 == 0 ? 0.35F : 0.25F;
                float secondaryColor = player.selectedItem == index ? 0.7F : index % 2 == 0 ? 0.25F : 0.35F;
                GLStateManager.setColor(color, color, color, 0.5F);
                RenderHelper.drawRect(40 * index, 0, 40, 40);
                GLStateManager.setColor(secondaryColor, secondaryColor, secondaryColor, 1.0F);
                RenderHelper.drawOutline(40 * index, 0, 40, 40, 2);
                if (stack != null) {
                    Pixel pixel = stack.getPixel();
                    GLStateManager.setColor(0.35F, 0.35F, 0.35F);
                    int x = 40 * index + 10;
                    RenderHelper.drawRect(x + 2, 12, 20, 20);
                    GLStateManager.setColor(pixel.getRed(), pixel.getGreen(), pixel.getBlue());
                    RenderHelper.drawRect(x, 10, 20, 20);
                    GLStateManager.setColor(1.0F, 1.0F, 1.0F);
                    RenderHelper.drawScaledStringWithShadow(x, 30, stack.getSize() + "", 1.0F);
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
    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        PixleClient pixle = PixleClient.INSTANCE;

        Level level = pixle.getLevel();
        PlayerEntity player = pixle.getPlayer();

        if (player != null) {
            int selectionX = pixle.getSelectionX(mouseX);
            int selectionY = pixle.getSelectionY(mouseY);

            int distX = (int) (selectionX - player.posX);
            int distY = (int) (selectionY - player.posY);
            double dist = Math.sqrt(distX * distX + distY * distY);

            Pixel pixel = Pixel.AIR;

            if (button == 1) {
                PixelStack pixelStack = player.getInventory().getPixelStack(player.selectedItem);
                if (pixelStack != null) {
                    pixel = pixelStack.getPixel();
                    pixelStack.increaseSize(-1);
                    if (pixelStack.getSize() <= 0) {
                        player.getInventory().setPixelStack(null, player.selectedItem);
                    }
                }
            }

            if (dist < PlayerEntity.REACH_DISTANCE) {
                for (PixelLayer layer : PixelLayer.values()) {
                    if (canSelect(level, selectionX, selectionY, layer) && level.getCollidingEntities(new PixelBounds(selectionX, selectionY)).isEmpty()) {
                        player.setPixel(pixel, selectionX, selectionY, layer);
                        break;
                    }
                }
            }

            for (int index = 0; index < 9; index++) {
                int x = index * 40;
                if (mouseX > x && mouseX < x + 40 && mouseY > 0 && mouseY < 40) {
                    player.selectedItem = index;
                    break;
                }
            }
        }
    }

    private boolean canSelect(Level level, int selectionX, int selectionY, PixelLayer layer) {
        boolean canSelect = level.hasPixel(selectionX, selectionY, layer);
        if (!canSelect) {
            outer: for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (x != y && x != -y) {
                        if (level.hasPixel(selectionX + x, selectionY + y, layer)) {
                            canSelect = true;
                            break outer;
                        }
                    }
                }
            }
        }
        return canSelect;
    }
}
