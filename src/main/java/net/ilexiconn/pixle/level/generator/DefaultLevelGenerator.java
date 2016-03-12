package net.ilexiconn.pixle.level.generator;

import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.pixel.Pixel;

public class DefaultLevelGenerator implements ILevelGenerator {
    @Override
    public void generate(Region region, int regionX, long seed) {
        for (int x = 0; x < Region.REGION_WIDTH; x++) {
            region.setPixel(Pixel.grass, x, 0);

            if (x % 4 == 0) {
                region.setPixel(Pixel.grass, x, 1);
            }
        }
    }
}
