package org.pixle.level.generator;

import org.pixle.level.Level;
import org.pixle.level.PixelLayer;
import org.pixle.pixel.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageStructureGenerator {
    private static Map<String, Structure> structureCache = new HashMap<>();

    public static void generateFromImage(Level level, int x, int y, String imagePath, boolean generateAir, boolean invertX) throws IOException {
        if (!imagePath.startsWith("/")) {
            imagePath = "/" + imagePath;
        }
        Structure structure = structureCache.get(imagePath);
        if (structure == null) {
            structure = loadStructure(imagePath);
            structureCache.put(imagePath, structure);
        }
        structure.generate(level, x, y, generateAir, invertX);
    }

    private static Structure loadStructure(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(ImageStructureGenerator.class.getResourceAsStream(imagePath));
        int width = image.getWidth();
        int height = image.getHeight();
        int[][][] pixels = new int[PixelLayer.values().length][width][height];
        for (int pixelY = 0; pixelY < height; pixelY++) {
            for (int pixelX = 0; pixelX < width; pixelX++) {
                int argb = image.getRGB(pixelX, pixelY);
                int alpha = (argb >> 24) & 0xFF;
                int color = argb & 0xFFFFFF;
                pixels[alpha > 128 ? PixelLayer.FOREGROUND.ordinal() : PixelLayer.BACKGROUND.ordinal()][pixelX][pixelY] = Pixel.fromColor(color).getPixelID();
            }
        }
        return new Structure(pixels);
    }
}
