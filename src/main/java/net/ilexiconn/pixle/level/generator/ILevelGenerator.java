package net.ilexiconn.pixle.level.generator;

import net.ilexiconn.pixle.level.region.Region;

import java.util.Random;

public interface ILevelGenerator {
    void generate(Region region, int regionX, long seed);
}
