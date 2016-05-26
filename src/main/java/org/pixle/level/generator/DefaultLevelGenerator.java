package org.pixle.level.generator;

import org.pixle.level.Level;
import org.pixle.level.PixelLayer;
import org.pixle.level.generator.tree.TreeGenerator;
import org.pixle.level.region.Region;
import org.pixle.pixel.Pixel;

import java.util.Random;

public class DefaultLevelGenerator implements ILevelGenerator {
    private static int fastFloor(double x) {
        int intX = (int) x;
        return x < intX ? intX - 1 : intX;
    }

    private static double cubicInterpolate(double[] p, double x) {
        return p[1] + 0.5 * x * (p[2] - p[0] + x * (2.0 * p[0] - 5.0 * p[1] + 4.0 * p[2] - p[3] + x * (3.0 * (p[1] - p[2]) + p[3] - p[0])));
    }

    @Override
    public void generate(Region region, int regionX, int regionY, long seed) {
        int regionOffsetX = regionX * Region.REGION_WIDTH;
        int regionStartY = regionY * Region.REGION_HEIGHT;
        for (int x = 0; x < Region.REGION_WIDTH; x++) {
            int worldX = x + regionOffsetX;

            float scaledX = worldX / 32.0F;

            int scaledXInt = fastFloor(scaledX);
            float fractionX = scaledX - scaledXInt;

            int height = (int) cubicInterpolate(new double[]{getHeight(seed, scaledXInt - 1), getHeight(seed, scaledXInt), getHeight(seed, scaledXInt + 1), getHeight(seed, scaledXInt + 2)}, fractionX);

            Random rand = new Random(worldX * seed);

            int dirtLayer = height - 20 - rand.nextInt(2);
            int grassLayer = height - rand.nextInt(2);

            int startY = Math.min(regionStartY, height);
            int endY = Math.min(height, regionStartY + Region.REGION_HEIGHT);
            for (int y = startY; y < endY; y++) {
                region.setPixel(getPixel(rand, dirtLayer, grassLayer, y), x, y & (Region.REGION_HEIGHT - 1), PixelLayer.FOREGROUND);
                region.setPixel(getPixel(rand, dirtLayer, grassLayer, y - 1), x, y & (Region.REGION_HEIGHT - 1), PixelLayer.BACKGROUND);
            }
        }
    }

    private Pixel getPixel(Random rand, int dirtLayer, int grassLayer, int y) {
        Pixel pixel = Pixel.STONE;
        if (y <= 0 || (y < 15 && rand.nextInt(y + 1) == 0)) {
            pixel = Pixel.BEDROCK;
        } else if (y >= dirtLayer) {
            if (y >= grassLayer - 2) {
                pixel = Pixel.GRASS;
            } else {
                if (rand.nextInt(30) == 0) {
                    pixel = Pixel.GRAVEL;
                } else {
                    pixel = Pixel.DIRT;
                }
            }
        }
        return pixel;
    }

    @Override
    public void decorate(Region region, int regionX, int regionY, Random rand) {
        int regionOffsetX = regionX * Region.REGION_WIDTH;
        Level level = region.getLevel();
        for (int i = 0; i < rand.nextInt(2); i++) {
            int x = rand.nextInt(Region.REGION_WIDTH);
            int worldX = x + regionOffsetX;
            TreeGenerator.generateTree(level, worldX, level.getGenerationHeight(worldX), rand);
        }
    }

    @Override
    public int getGenerationHeight(int x, long seed) {
        return getHeight(seed, fastFloor(x / 32.0));
    }

    private int getHeight(long seed, int x) {
        return new Random(seed * x).nextInt(20) + Level.LEVEL_HEIGHT / 2;
    }
}
