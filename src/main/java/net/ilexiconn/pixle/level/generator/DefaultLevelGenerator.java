package net.ilexiconn.pixle.level.generator;

import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.level.generator.tree.TreeGenerator;
import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.pixel.Pixel;

import java.util.Random;

public class DefaultLevelGenerator implements ILevelGenerator {
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
            int dirtLayer = height - 10;

            Random bedrockRand = new Random(worldX * seed);

            for (int y = 0; y < height; y++) {
                if (y >= regionStartY && y < regionStartY + Region.REGION_HEIGHT) {
                    Pixel pixel = Pixel.stone;
                    if (y == 0 || (y < 15 && bedrockRand.nextInt(y + 1) == 0)) {
                        pixel = Pixel.bedrock;
                    } else if (y >= dirtLayer) {
                        if (y == height - 1) {
                            pixel = Pixel.grass;
                        } else {
                            pixel = Pixel.dirt;
                        }
                    }
                    region.setPixel(pixel, x, y & (Region.REGION_HEIGHT - 1), PixelLayer.FOREGROUND);
                }
            }
        }
    }

    @Override
    public void decorate(Region region, int regionX, int regionY, Random rand) {
        int regionOffsetX = regionX * Region.REGION_WIDTH;
        Level level = region.getLevel();
        for (int i = 0; i < rand.nextInt(2); i++) {
            int x = rand.nextInt(Region.REGION_WIDTH);
            int worldX = x + regionOffsetX;
            TreeGenerator.generateTree(level, worldX, level.getHeight(worldX, PixelLayer.FOREGROUND), rand);
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
        return new Random(seed * x).nextInt(20) + Level.LEVEL_HEIGHT / 2;
    }
}
