package net.ilexiconn.pixle.level.generator;

import net.ilexiconn.pixle.level.Level;

public class Structure {
    private int[][] pixels;
    private int width;
    private int height;

    public Structure(int[][] pixels) {
        this.pixels = pixels;
        this.width = pixels.length;
        this.height = pixels[0].length;
    }

    public void generate(Level level, int x, int y, boolean generateAir) {
        for (int pixelY = 0; pixelY < height; pixelY++) {
            for (int pixelX = 0; pixelX < width; pixelX++) {
                int pixel = pixels[pixelX][pixelY];
                if ((pixel != 0x0094FF && pixel != 0xFFFFFF) || generateAir) {
                    level.setPixel(pixel, (x + pixelX) - (width / 2), y + (height - pixelY));
                }
            }
        }
    }
}
