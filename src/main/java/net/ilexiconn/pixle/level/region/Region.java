package net.ilexiconn.pixle.level.region;

import net.darkhax.opennbt.tags.CompoundTag;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.level.generator.ILevelGenerator;
import net.ilexiconn.pixle.pixel.Pixel;

import java.util.Random;

public class Region {
    public static final int REGION_WIDTH = 16;
    public static final int REGION_HEIGHT = 1024;

    private Level level;
    private int[][][] pixels = new int[PixelLayer.values().length][REGION_WIDTH][REGION_HEIGHT];
    private int[][] heights = new int[PixelLayer.values().length][REGION_WIDTH];
    private int x;

    public Region(int x, Level level) {
        this.level = level;
        this.x = x;
    }

    public Pixel getPixel(int x, int y, PixelLayer layer) {
        if (x >= 0 && x < REGION_WIDTH && y >= 0 && y < REGION_HEIGHT) {
            return Pixel.getPixelByID(pixels[layer.ordinal()][x][y]);
        } else {
            return Pixel.air;
        }
    }

    public void setPixel(Pixel pixel, int x, int y, PixelLayer layer) {
        if (x >= 0 && x < REGION_WIDTH && y >= 0 && y < REGION_HEIGHT) {
            int layerOrdinal = layer.ordinal();
            pixels[layerOrdinal][x][y] = pixel.getPixelID();
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
                pixels[layer][x] = layerTag.getIntArray(x + "");
            }
        }
    }

    public int getHeight(int x, PixelLayer layer) {
        return heights[layer.ordinal()][x];
    }

    public int[][][] getPixels() {
        return pixels;
    }

    public void setPixels(int[][] pixels, PixelLayer layer, int yOffset) {
        for (int x = 0; x < pixels.length; x++) {
            System.arraycopy(pixels[x], 0, this.pixels[layer.ordinal()][x], yOffset, 16);
        }
        loadHeights(layer);
    }

    private void loadHeights(PixelLayer layer) {
        int layerIndex = layer.ordinal();
        for (int x = 0; x < pixels[layerIndex].length; x++) {
            for (int y = REGION_HEIGHT - 1; y >= 0; y--) {
                Pixel pixel = Pixel.getPixelByID(pixels[layerIndex][x][y]);
                if (pixel != Pixel.air) {
                    heights[layerIndex][x] = y;
                }
            }
        }
    }

    public int getX() {
        return x;
    }
}
