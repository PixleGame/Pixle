package net.ilexiconn.pixle.level.generator;

import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.generator.tree.TreeGenerator;
import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.pixel.Pixel;

import java.util.Random;

public class DefaultLevelGenerator implements ILevelGenerator {
    @Override
    public void generate(Region region, int regionX, long seed) {
        int regionOffset = regionX * Region.REGION_WIDTH;
        for (int x = 0; x < Region.REGION_WIDTH; x++) {
            int worldX = x + regionOffset;

            float scaledX = worldX / 16.0F;

            int scaledXInt = fastFloor(scaledX);
            float fractionX = scaledX - scaledXInt;

            int height = (int) cubicInterpolate(new double[] { getHeight(seed, scaledXInt - 1), getHeight(seed, scaledXInt), getHeight(seed, scaledXInt + 1), getHeight(seed, scaledXInt + 2) }, fractionX);
            int dirtLayer = height - 10;

            for (int y = 0; y < height; y++) {
                Pixel pixel = Pixel.stone;
                if (y >= dirtLayer) {
                    if (y == height - 1) {
                        pixel = Pixel.grass;
                    } else {
                        pixel = Pixel.dirt;
                    }
                }
                region.setPixel(pixel, x, y);
            }
        }
    }

    @Override
    public void decorate(Region region, int regionX, Random rand) {
        int regionOffsetX = regionX * Region.REGION_WIDTH;
        Level level = region.getLevel();
        for (int i = 0; i < rand.nextInt(2); i++) {
            int x = rand.nextInt(Region.REGION_WIDTH);
            TreeGenerator.generateTree(level, x + regionOffsetX, region.getHeight(x), rand);
        }
    }

    private static int fastFloor(double x) {
        int intX = (int) x;
        return x < intX ? intX - 1 : intX;
    }

    private static double cubicInterpolate(double[] p, double x) {
        return p[1] + 0.5 * x * (p[2] - p[0] + x * (2.0 * p[0] - 5.0 * p[1] + 4.0 * p[2] - p[3] + x * (3.0 * (p[1] - p[2]) + p[3] - p[0])));
    }

    private int getHeight(long seed, int x) {
        return new Random(seed * x).nextInt(10) + 60;
    }
}
