package net.ilexiconn.pixle.level.region;

import net.darkhax.opennbt.tags.CompoundTag;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.pixel.Pixel;

import java.util.Random;

public class Region {
    private Level level;
    private int[][] pixels = new int[16][256];
    private int x;

    public Region(int x, Level level) {
        this.level = level;
        this.x = x;
    }

    public Pixel getPixel(int x, int y) {
        if (x >= 0 && x <= 15 && y >= 0 && y <= 255) {
            return Pixel.getPixelByID(pixels[x][y]);
        } else {
            return null;
        }
    }

    public void setPixel(Pixel pixel, int x, int y) {
        pixels[x][y] = pixel.getPixelID();
    }

    public void generate(long seed) {
        level.getLevelGenerator().generate(this, x, new Random(seed * x));
    }

    public Level getLevel() {
        return level;
    }

    public void writeToNBT(CompoundTag compound) {
        compound.setInt("regionX", x);
        for (int x = 0; x < pixels.length; x++) {
            compound.setIntArray(x + "", pixels[x]);
        }
    }

    public void readFromNBT(CompoundTag compound) {
        for (int x = 0; x < pixels.length; x++) {
            pixels[x] =  compound.getIntArray(x + "");
        }
    }
}
