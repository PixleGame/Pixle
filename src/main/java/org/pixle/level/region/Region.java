package org.pixle.level.region;

import net.darkhax.opennbt.tags.CompoundTag;
import org.pixle.event.GenerateRegionEvent;
import org.pixle.event.bus.EventBus;
import org.pixle.level.Level;
import org.pixle.level.PixelLayer;
import org.pixle.level.generator.ILevelGenerator;
import org.pixle.pixel.Pixel;

import java.util.Random;

public class Region {
    public static final int REGION_WIDTH = 32;
    public static final int REGION_HEIGHT = 32;

    private Level level;
    private int[][][] pixels = new int[PixelLayer.values().length][REGION_WIDTH][REGION_HEIGHT];
    private int x;
    private int y;
    private boolean[] empty = new boolean[PixelLayer.values().length];
    private boolean loaded;

    public Region(int x, int y, Level level) {
        this.level = level;
        this.x = x;
        this.y = y;
        for (int layer = 0; layer < empty.length; layer++) {
            empty[layer] = true;
        }
        this.loaded = false;
    }

    public Pixel getPixel(int x, int y, PixelLayer layer) {
        if (x >= 0 && x < REGION_WIDTH && y >= 0 && y < REGION_HEIGHT) {
            return Pixel.getPixelByID(pixels[layer.ordinal()][x][y]);
        } else {
            return Pixel.AIR;
        }
    }

    public int getPixel(int x, int y, int layer) {
        return pixels[layer][x][y];
    }

    public void setPixel(Pixel pixel, int x, int y, PixelLayer layer) {
        if (x >= 0 && x < REGION_WIDTH && y >= 0 && y < REGION_HEIGHT) {
            int layerOrdinal = layer.ordinal();
            pixels[layerOrdinal][x][y] = pixel.getPixelID();
            if (pixel != Pixel.AIR) {
                empty[layerOrdinal] = false;
            } else {
                checkEmpty();
            }
        }
    }

    public void generate(long seed) {
        ILevelGenerator levelGenerator = level.getLevelGenerator();
        EventBus eventBus = EventBus.get();
        if (eventBus.post(new GenerateRegionEvent.Pre(level, this, levelGenerator, seed))) {
            levelGenerator.generate(this, x, y, seed);
            levelGenerator.decorate(this, x, y, new Random(seed * x));
        }
        eventBus.post(new GenerateRegionEvent.Post(level, this, levelGenerator, seed));
        loaded = true;
    }

    public Level getLevel() {
        return level;
    }

    public void writeToNBT(CompoundTag compound) {
        compound.setInt("regionX", x);
        compound.setByte("regionY", (byte) y);
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
        loaded = true;
        checkEmpty();
    }

    public int[][][] getPixels() {
        return pixels;
    }

    public void setPixels(int[][][] pixels) {
        this.pixels = pixels;
        checkEmpty();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPixels(byte[][] pixels, int layer) {
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++) {
                this.pixels[layer][x][y] = pixels[x][y];
            }
        }
        checkEmpty();
    }

    private void checkEmpty() {
        for (int layer = 0; layer < pixels.length; layer++) {
            boolean empty = true;
            for (int x = 0; x < REGION_WIDTH && empty; x++) {
                for (int y = 0; y < REGION_HEIGHT; y++) {
                    if (pixels[layer][x][y] != 0) {
                        empty = false;
                        break;
                    }
                }
            }
            this.empty[layer] = empty;
        }
    }

    public boolean isEmpty(PixelLayer layer) {
        return empty[layer.ordinal()];
    }

    public void setLoaded() {
        this.loaded = true;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
