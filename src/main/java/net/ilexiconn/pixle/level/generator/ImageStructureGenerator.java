package net.ilexiconn.pixle.level.generator;

import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.pixel.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageStructureGenerator {
    private static Map<String, Structure> structureCache = new HashMap<>();

    public static void generateFromImage(Level level, int x, int y, String imagePath, boolean generateAir) throws IOException {
        if (!imagePath.startsWith("/")) {
            imagePath = "/" + imagePath;
        }
        Structure structure = structureCache.get(imagePath);
        if (structure == null) {
            structure = loadStructure(imagePath);
            structureCache.put(imagePath, structure);
        }
        structure.generate(level, x, y, generateAir);
    }

    private static Structure loadStructure(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(ImageStructureGenerator.class.getResourceAsStream(imagePath));
        int width = image.getWidth();
        int height = image.getHeight();
        Pixel[][] pixels = new Pixel[width][height];
        for (int pixelY = 0; pixelY < height; pixelY++) {
            for (int pixelX = 0; pixelX < width; pixelX++) {
                pixels[pixelX][pixelY] = Pixel.fromColor(image.getRGB(pixelX, pixelY) & 0xFFFFFF);
            }
        }
        return new Structure(pixels);
    }
}
