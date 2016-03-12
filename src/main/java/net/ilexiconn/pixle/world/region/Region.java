package net.ilexiconn.pixle.world.region;

import net.darkhax.opennbt.tags.CompoundTag;
import net.ilexiconn.pixle.world.World;
import net.ilexiconn.pixle.world.pixel.Pixel;

import java.util.Random;

public class Region {
    private World world;
    private Pixel[][] pixels = new Pixel[16][256];
    private int x;

    public Region(int x, World world) {
        this.world = world;
        this.x = x;
    }

    public Pixel getPixel(int x, int y) {
        if (x >= 0 && x < 16 && y >= 0 && y <= 255) {
            return pixels[x][y];
        } else {
            return null;
        }
    }

    public void setPixel(Pixel pixel, int x, int y) {
        pixels[x][y] = pixel;
    }

    public void generate(long seed) {
        world.getWorldGenerator().generate(this, x, new Random(seed * x));
    }

    public World getWorld() {
        return world;
    }

    public void writeToNBT(CompoundTag compound) {
        compound.setInt("regionX", x);
        for (int x = 0; x < pixels.length; x++) {
            Pixel[] pixelArray = pixels[x];
            int[] pixelArrayInt = new int[pixelArray.length];
            for (int y = 0; y < pixelArray.length; y++) {
                pixelArrayInt[y] = pixelArray[y].getPixelID();
            }
            compound.setIntArray(x + "", pixelArrayInt);
        }
    }

    public void readFromNBT(CompoundTag compound) {
        for (int x = 0; x < pixels.length; x++) {
            int[] pixelIntArray = compound.getIntArray(x + "");
            Pixel[] pixelArray = new Pixel[pixelIntArray.length];
            for (int y = 0; y < pixelArray.length; y++) {
                pixelArray[y] = Pixel.getPixelByID(pixelIntArray[y]);
            }
            pixels[x] = pixelArray;
        }
    }
}
