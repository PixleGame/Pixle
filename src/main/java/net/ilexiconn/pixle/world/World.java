package net.ilexiconn.pixle.world;

import net.darkhax.opennbt.tags.CompoundTag;
import net.darkhax.opennbt.tags.Tag;
import net.ilexiconn.pixle.world.entity.Entity;
import net.ilexiconn.pixle.world.entity.EntityRegistry;
import net.ilexiconn.pixle.world.generator.IWorldGenerator;
import net.ilexiconn.pixle.world.generator.WorldGeneratorDefault;
import net.ilexiconn.pixle.world.pixel.Pixel;
import net.ilexiconn.pixle.world.region.Region;

import java.util.ArrayList;
import java.util.List;

public class World {
    private static final int WORLD_REGION_WIDTH = 62500;

    private Region[] regions = new Region[WORLD_REGION_WIDTH];

    private IWorldGenerator worldGenerator;
    private long seed;

    private List<Entity> entityList = new ArrayList<>();

    public World(long seed) {
        this.worldGenerator = new WorldGeneratorDefault();
        this.seed = seed;
    }

    public World(String seed) {
        this(seed.hashCode());
    }

    public World() {
        this(System.nanoTime());
    }

    public void setPixel(Pixel pixel, int x, int y) {
        getRegionForPixel(x).setPixel(pixel, x % 15, y);
    }

    public Pixel getPixel(int x, int y) {
        return getRegionForPixel(x).getPixel(x % 15, y);
    }

    public int getRegionX(int x) {
        return x >> 4;
    }

    public Region getRegionForPixel(int x) {
        return getRegion(getRegionX(x));
    }

    public Region getRegion(int x) {
        Region region = regions[getRegionIndex(x)];
        if (region == null) {
            region = new Region(x, this);
            region.generate(seed);
            setRegion(region, x);
        }
        return region;
    }

    public void setRegion(Region region, int x) {
        regions[getRegionIndex(x)] = region;
    }

    private int getRegionIndex(int x) {
        return x + (WORLD_REGION_WIDTH / 2);
    }

    public IWorldGenerator getWorldGenerator() {
        return worldGenerator;
    }

    public void writeToNBT(CompoundTag compound) {
        List<Tag> tagList = new ArrayList<>();
        for (Entity entity : entityList) {
            CompoundTag compoundTag = new CompoundTag("");
            entity.writeToNBT(compoundTag);
            tagList.add(compoundTag);
        }
        compound.setTagList("entities", tagList);
    }

    public void readFromNBT(CompoundTag compound) {
        List<Tag> tagList = compound.getTagList("entities");
        for (Tag tag : tagList) {
            CompoundTag compoundTag = (CompoundTag) tag;
            Entity entity = EntityRegistry.initializeEntity(compoundTag.getByte("id"), this);
            if (entity != null) {
                entity.readFromNBT(compoundTag);
                entityList.add(entity);
            }
        }
    }
}
