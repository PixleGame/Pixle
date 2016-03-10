package net.ilexiconn.pixle.world.region;

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
        return pixels[x][y];
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
}
