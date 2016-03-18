package org.pixle.level.generator;

import org.pixle.level.Level;
import org.pixle.level.PixelLayer;
import org.pixle.pixel.Pixel;

public class Structure {
    private int[][][] pixels;
    private int width;
    private int height;
    private int bottomY;

    public Structure(int[][][] pixels) {
        this.pixels = pixels;
        this.width = pixels[0].length;
        this.height = pixels[0][0].length;
        bottomY = 0;
        for (int layer = 0; layer < pixels.length; layer++) {
            for (int pixelY = 0; pixelY < height; pixelY++) {
                for (int pixelX = 0; pixelX < width; pixelX++) {
                    if (!isAir(pixels[layer][pixelX][pixelY])) {
                        if (pixelY > bottomY) {
                            bottomY = pixelY;
                        }
                    }
                }
            }
        }
    }

    public void generate(Level level, int x, int y, boolean generateAir, boolean invertX) {
        while (!hasBase(level, x, y, invertX) && y > 0) {
            y--;
        }
        for (int layer = 0; layer < pixels.length; layer++) {
            for (int pixelY = 0; pixelY < height; pixelY++) {
                for (int pixelX = 0; pixelX < width; pixelX++) {
                    int pixel = pixels[layer][pixelX][pixelY];
                    if (!isAir(pixel) || generateAir) {
                        level.setPixel(Pixel.getPixelByID(pixel), (invertX ? (width - pixelX) : pixelX) + x, y + (height - pixelY), PixelLayer.values()[layer]);
                    }
                }
            }
        }
    }

    private boolean hasBase(Level level, int x, int y, boolean invertX) {
        PixelLayer baseLayer = PixelLayer.FOREGROUND;
        for (PixelLayer layer : PixelLayer.values()) {
            for (int pixelX = 0; pixelX < width; pixelX++) {
                int worldX = x + (invertX ? (width - pixelX) : pixelX);
                int pixel = pixels[layer.ordinal()][pixelX][bottomY];
                if (!isAir(pixel)) {
                    if (level.getPixel(worldX, y + (height - bottomY) - 1, baseLayer) == Pixel.AIR) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isAir(int pixel) {
        return pixel == 0;
    }
}
