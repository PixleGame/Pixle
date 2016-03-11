package net.ilexiconn.pixle.world.region;

import net.ilexiconn.pixle.world.World;
import net.ilexiconn.pixle.world.pixel.Pixel;

import java.util.Random;

public class Region {
    private World world;
    private int[][] pixels = new int[16][256];
    private int x;

    public Region(int x, World world) {
        this.world = world;
        this.x = x;
    }

    public Pixel getPixel(int x, int y) {
        if (y >= 0) {
            return Pixel.getPixelByID(pixels[x][y]);
        } else {
            return null;
        }
    }

    public void setPixel(Pixel pixel, int x, int y) {
        pixels[x][y] = pixel.getPixelID();
    }

    public void generate(long seed) {
        world.getWorldGenerator().generate(this, x, new Random(seed * x));
    }

    public World getWorld() {
        return world;
    }
}
