package net.ilexiconn.pixle.level.generator;

import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.pixel.Pixel;

import java.util.Random;

public class DefaultLevelGenerator implements ILevelGenerator {
    @Override
    public void generate(Region region, int regionX, long seed) {
        for (int x = 0; x < Region.REGION_WIDTH; x++) {
            int worldX = x + (regionX * Region.REGION_WIDTH);

            float scaledX = worldX / 8.0F;

            int scaledXInt = (int) scaledX;
            float fractionX = scaledX - scaledXInt;

            int height = (int) cubicInterpolate(new double[] { getHeight(seed, scaledXInt - 1), getHeight(seed, scaledXInt), getHeight(seed, scaledXInt + 1), getHeight(seed, scaledXInt + 2) }, fractionX);

            for (int y = 0; y < height; y++) {
                region.setPixel(Pixel.grass, x, y);
            }
        }
    }

    private static double cubicInterpolate(double[] p, double x) {
        return p[1] + 0.5 * x * (p[2] - p[0] + x * (2.0 * p[0] - 5.0 * p[1] + 4.0 * p[2] - p[3] + x * (3.0 * (p[1] - p[2]) + p[3] - p[0])));
    }

    private int getHeight(long seed, int x) {
        return new Random(seed * x).nextInt(10) + 10;
    }
}
