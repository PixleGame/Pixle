package net.ilexiconn.pixle.event;

import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.generator.ILevelGenerator;
import net.ilexiconn.pixle.level.region.Region;

public class GenerateRegionEvent extends Event {
    private Level level;
    private Region region;
    private ILevelGenerator levelGenerator;
    private long seed;

    public GenerateRegionEvent(Level level, Region region, ILevelGenerator levelGenerator, long seed) {
        this.level = level;
        this.region = region;
        this.levelGenerator = levelGenerator;
        this.seed = seed;
    }

    public Level getLevel() {
        return level;
    }

    public Region getRegion() {
        return region;
    }

    public ILevelGenerator getLevelGenerator() {
        return levelGenerator;
    }

    public long getSeed() {
        return seed;
    }

    @Cancelable
    public static class Pre extends GenerateRegionEvent {
        public Pre(Level level, Region region, ILevelGenerator levelGenerator, long seed) {
            super(level, region, levelGenerator, seed);
        }
    }

    public static class Post extends GenerateRegionEvent {
        public Post(Level level, Region region, ILevelGenerator levelGenerator, long seed) {
            super(level, region, levelGenerator, seed);
        }
    }
}
