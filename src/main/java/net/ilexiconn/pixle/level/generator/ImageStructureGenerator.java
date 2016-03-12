package net.ilexiconn.pixle.level.generator;

import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.pixel.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageStructureGenerator {
    public static void generateFromImage(Level level, int x, int y, String imagePath) throws IOException {
        if (!imagePath.startsWith("/")) {
            imagePath = "/" + imagePath;
        }
        BufferedImage image = ImageIO.read(ImageStructureGenerator.class.getResourceAsStream(imagePath));
        int width = image.getWidth();
        int height = image.getHeight();
        for (int pixelY = 0; pixelY < height; pixelY++) {
            for (int pixelX = 0; pixelX < width; pixelX++) {
                int color = image.getRGB(pixelX, pixelY) & 0xFFFFFF;
                Pixel pixel = Pixel.fromColor(color);
                if (pixel != Pixel.air) {
                    level.setPixel(pixel, (x + pixelX) - (width / 2), y + (height - pixelY));
                }
            }
        }
    }
}
