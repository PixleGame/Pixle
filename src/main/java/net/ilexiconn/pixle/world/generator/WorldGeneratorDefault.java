package net.ilexiconn.pixle.world.generator;

import net.ilexiconn.pixle.world.pixel.Pixel;
import net.ilexiconn.pixle.world.region.Region;

import java.util.Random;

public class WorldGeneratorDefault implements IWorldGenerator {
    @Override
    public void generate(Region region, int regionX, Random rand) {
        for (int x = 0; x < 16; x++) {
            region.setPixel(Pixel.grass, x, 0);

            if (x % 4 == 0) {
                region.setPixel(Pixel.grass, x, 1);
            }
        }
    }
}
