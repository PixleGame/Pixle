package net.ilexiconn.pixle.level.region;

import net.darkhax.opennbt.tags.CompoundTag;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.level.generator.ILevelGenerator;

import java.util.Random;

public class Region {
    public static final int REGION_WIDTH = 64;
    public static final int REGION_HEIGHT = 1024;

    private Level level;
    private int[][][] pixels = new int[PixelLayer.values().length][REGION_WIDTH][REGION_HEIGHT];
    private int[][] heights = new int[PixelLayer.values().length][REGION_WIDTH];
    private int x;

    public Region(int x, Level level) {
        this.level = level;
        this.x = x;
    }

    public int getPixel(int x, int y, PixelLayer layer) {
        if (x >= 0 && x < REGION_WIDTH && y >= 0 && y < REGION_HEIGHT) {
            return pixels[layer.ordinal()][x][y];
        } else {
            return 0x0094FF;
        }
    }

    public void setPixel(int pixel, int x, int y, PixelLayer layer) {
        if (x >= 0 && x < REGION_WIDTH && y >= 0 && y < REGION_HEIGHT) {
            int layerOrdinal = layer.ordinal();
            pixels[layerOrdinal][x][y] = pixel;
            if (y > heights[layerOrdinal][x]) {
                heights[layerOrdinal][x] = y;
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
        for (int layer = 0; layer < pixels.length; layer++) {
            CompoundTag layerTag = new CompoundTag("layer" + layer);
            int[][] pixelsForLayer = pixels[layer];
            for (int x = 0; x < pixelsForLayer.length; x++) {
                layerTag.setIntArray(x + "", pixelsForLayer[x]);
            }
            compound.setTag(layerTag);
        }
    }

    public void readFromNBT(CompoundTag compound) {
        for (int layer = 0; layer < pixels.length; layer++) {
            CompoundTag layerTag = compound.getCompoundTag("layer" + layer);
            for (int x = 0; x < pixels[layer].length; x++) {
                pixels[layer][x] =  layerTag.getIntArray(x + "");
            }
        }
    }

    public int getHeight(int x, PixelLayer layer) {
        return heights[layer.ordinal()][x];
    }
}
