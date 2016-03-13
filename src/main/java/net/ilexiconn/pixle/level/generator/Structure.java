package net.ilexiconn.pixle.level.generator;

import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.pixel.Pixel;

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
                    if ((pixel != Pixel.AIR && pixel != 0xFFFFFF && pixel != 0) || generateAir) {
                        level.setPixel(pixel, (x + pixelX) - (width / 2), y + (height - pixelY), PixelLayer.values()[layer]);
                    }
                }
            }
        }
    }
}
