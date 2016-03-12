package net.ilexiconn.pixle.level.generator;

import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.pixel.Pixel;

public class Structure {
    private Pixel[][] pixels;
    private int width;
    private int height;

    public Structure(Pixel[][] pixels) {
        this.pixels = pixels;
        this.width = pixels.length;
        this.height = pixels[0].length;
    }

    public void generate(Level level, int x, int y, boolean generateAir) {
        for (int pixelY = 0; pixelY < height; pixelY++) {
            for (int pixelX = 0; pixelX < width; pixelX++) {
                Pixel pixel = pixels[pixelX][pixelY];
                if (pixel != Pixel.air || generateAir) {
                    level.setPixel(pixel, (x + pixelX) - (width / 2), y + (height - pixelY));
                }
            }
        }
    }
}
