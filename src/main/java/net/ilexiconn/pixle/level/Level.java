package net.ilexiconn.pixle.level;

import net.darkhax.opennbt.tags.CompoundTag;
import net.darkhax.opennbt.tags.Tag;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.EntityRegistry;
import net.ilexiconn.pixle.level.generator.ILevelGenerator;
import net.ilexiconn.pixle.level.generator.DefaultLevelGenerator;
import net.ilexiconn.pixle.pixel.Pixel;
import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.util.Bounds;
import net.ilexiconn.pixle.util.PixelBounds;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private static final int LEVEL_REGION_WIDTH = 62500;

    private Region[] regions = new Region[LEVEL_REGION_WIDTH];

    private ILevelGenerator levelGenerator;
    private long seed;

    private List<Entity> entities = new ArrayList<>();

    public static final int PIXEL_SIZE = 6;

    public Level(long seed) {
        this.levelGenerator = new DefaultLevelGenerator();
        this.seed = seed;
    }

    public Level(String seed) {
        this(seed.hashCode());
    }

    public Level() {
        this(System.nanoTime());
    }

    public void setPixel(Pixel pixel, int x, int y) {
        getRegionForPixel(x).setPixel(pixel, x % (Region.REGION_WIDTH - 1), y);
    }

    public Pixel getPixel(int x, int y) {
        Region region = getRegionForPixel(x);
        if (x < 0) {
            x = Region.REGION_WIDTH - x;
        }
        x = x % (Region.REGION_WIDTH - 1);
        return region.getPixel(x, y);
    }

    public boolean hasPixel(int x, int y) {
        return getPixel(x, y) != Pixel.air;
    }

    public int getRegionX(int x) {
        return x >> 6;
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
        return x + (LEVEL_REGION_WIDTH / 2);
    }

    public ILevelGenerator getLevelGenerator() {
        return levelGenerator;
    }

    public void update() {
        for (Entity entity : entities) {
            entity.update();
        }
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    public List<Entity> getEntities() {
        return new ArrayList<>(entities);
    }

    public List<Bounds> getIntersectingPixelBounds(Bounds bounds) {
        List<Bounds> colliding = new ArrayList<Bounds>();
        for (int y = (int) bounds.getMinY() - 1; y < Math.ceil(bounds.getMaxY() + 1); y++) {
            for (int x = (int) bounds.getMinX() - 1; x < Math.ceil(bounds.getMaxX() + 1); x++) {
                if (hasPixel(x, y)) {
                    PixelBounds pixelBounds = new PixelBounds(x, y);
                    if (pixelBounds.intersects(bounds)) {
                        colliding.add(pixelBounds);
                    }
                }
            }
        }
        return colliding;
    }

    public void writeToNBT(CompoundTag compound) {
        List<Tag> tagList = new ArrayList<>();
        for (Entity entity : entities) {
            CompoundTag compoundTag = new CompoundTag("");
            entity.writeToNBT(compoundTag);
            tagList.add(compoundTag);
        }
        compound.setTagList("entities", tagList);
        tagList.clear();
        for (Region region : regions) {
            if (region != null) {
                CompoundTag compoundTag = new CompoundTag("");
                region.writeToNBT(compoundTag);
                tagList.add(compoundTag);
            }
        }
        compound.setTagList("regions", tagList);
    }

    public void readFromNBT(CompoundTag compound) {
        List<Tag> tagList = compound.getTagList("entities");
        for (Tag tag : tagList) {
            CompoundTag compoundTag = (CompoundTag) tag;
            Entity entity = EntityRegistry.initializeEntity(compoundTag.getByte("id"), this);
            if (entity != null) {
                entity.readFromNBT(compoundTag);
                addEntity(entity);
            }
        }
        tagList = compound.getTagList("regions");
        for (Tag tag : tagList) {
            CompoundTag compoundTag = (CompoundTag) tag;
            int regionX = compoundTag.getInt("regionX");
            Region region = new Region(regionX, this);
            region.readFromNBT(compoundTag);
            regions[regionX] = region;
        }
    }
}
