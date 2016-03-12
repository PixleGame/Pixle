package net.ilexiconn.pixle.client.gui;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.client.gl.GLStateManager;
import net.ilexiconn.pixle.client.render.RenderHelper;
import net.ilexiconn.pixle.client.render.RenderingRegistry;
import net.ilexiconn.pixle.client.render.entity.IEntityRenderer;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.pixel.Pixel;
import org.lwjgl.opengl.Display;

public class WorldGUI extends GUI {
    public WorldGUI(PixleClient pixle) {
        super(pixle);
    }

    @Override
    public void render() {
        Level level = pixle.getLevel();
        PlayerEntity player = pixle.getPlayer();

        int pixelSize = Level.PIXEL_SIZE;
        int pixelsInWidth = (int) Math.ceil(Display.getWidth() / pixelSize);

        int centerX = Display.getWidth() / 2;
        int centerY = Display.getHeight() / 2;

        GLStateManager.setColor(0x0094FF);
        RenderHelper.drawRect(0, 0, Display.getWidth(), (int) (Display.getHeight() - (centerY - ((player.posY + 1) * pixelSize))));

        for (int y = 0; y < 256; y++) {
            for (int x = (int) (player.posX - (pixelsInWidth / 2)) - 1; x < player.posX + (pixelsInWidth / 2) + 1; x++) {
                if (level.hasPixel(x, y)) {
                    Pixel pixel = level.getPixel(x, y);
                    GLStateManager.setColor(pixel.getColor());
                    RenderHelper.drawRect((int) (centerX - Math.round((player.posX - x) * pixelSize)), Display.getHeight() - (centerY - (int) ((player.posY - y) * pixelSize)), pixelSize, pixelSize);
                }
            }
        }

        for (Entity entity : level.getEntities()) {
            IEntityRenderer entityRenderer = RenderingRegistry.getEntityRenderer(entity.getClass());
            if (entityRenderer != null) {
                entityRenderer.render(entity, centerX - (int) ((entity.posX - player.posX) * pixelSize), centerY - (int) ((entity.posY - player.posY) * pixelSize), level, (float) pixle.getDelta());
            }
        }
    }
}
