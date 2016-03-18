package org.pixle.level.generator;

import org.pixle.level.Level;
import org.pixle.level.PixelLayer;
import org.pixle.pixel.Pixel;

public class Structure {
    private int[][][] pixels;
    private int width;
    private int height;

    public Structure(int[][][] pixels) {
        this.pixels = pixels;
        this.width = pixels[0].length;
        this.height = pixels[0][0].length;
    }

    public void generate(Level level, int x, int y, boolean generateAir) {
        for (int layer = 0; layer < pixels.length; layer++) {
            for (int pixelY = 0; pixelY < height; pixelY++) {
                for (int pixelX = 0; pixelX < width; pixelX++) {
                    int pixel = pixels[layer][pixelX][pixelY];
                    if (!isAir(pixel) || generateAir) {
                        if (pixel == 0 || pixel == 0xFFFFFF) {
                            pixel = Pixel.AIR.getColor();
                        }
                        level.setPixel(Pixel.getPixelByID(pixel), (x + pixelX) - (width / 2), y + (height - pixelY), PixelLayer.values()[layer]);
                    }
                }
            }
        }
    }

    private boolean isAir(int pixel) {
        return pixel == Pixel.AIR.getColor() || pixel == 0xFFFFFF || pixel == 0;
    }
}
