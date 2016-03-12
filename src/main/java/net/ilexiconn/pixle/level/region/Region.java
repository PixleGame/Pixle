package net.ilexiconn.pixle.level.region;

import net.darkhax.opennbt.tags.CompoundTag;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.generator.ILevelGenerator;
import net.ilexiconn.pixle.pixel.Pixel;

import java.util.Random;

public class Region {
    public static final int REGION_WIDTH = 64;

    private Level level;
    private int[][] pixels = new int[REGION_WIDTH][256];
    private int[] heights = new int[REGION_WIDTH];
    private int x;

    public Region(int x, Level level) {
        this.level = level;
        this.x = x;
    }

    public Pixel getPixel(int x, int y) {
        if (x >= 0 && x < REGION_WIDTH && y >= 0 && y <= 255) {
            return Pixel.getPixelByID(pixels[x][y]);
        } else {
            return Pixel.air;
        }
    }

    public void setPixel(Pixel pixel, int x, int y) {
        if (x >= 0 && x < REGION_WIDTH && y >= 0 && y <= 255) {
            pixels[x][y] = pixel.getPixelID();
            if (y > heights[x]) {
                heights[x] = y;
            }
        }
    }

    public void generate(long seed) {
        ILevelGenerator levelGenerator = level.getLevelGenerator();
        levelGenerator.generate(this, x, seed);
        levelGenerator.decorate(this, x, new Random(seed * x));
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

    public int getHeight(int x) {
        return heights[x];
    }
}
