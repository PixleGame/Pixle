package net.ilexiconn.pixle.world.generator;

import net.ilexiconn.pixle.world.region.Region;

import java.util.Random;

public interface IWorldGenerator {
    void generate(Region region, int regionX, Random rand);

}
