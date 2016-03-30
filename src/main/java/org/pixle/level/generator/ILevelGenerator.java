package org.pixle.level.generator;

import org.pixle.level.region.Region;

import java.util.Random;

public interface ILevelGenerator {
    void generate(Region region, int regionX, int regionY, long seed);

    void decorate(Region region, int regionX, int regionY, Random rand);

    int getGenerationHeight(int x, long seed);
}
