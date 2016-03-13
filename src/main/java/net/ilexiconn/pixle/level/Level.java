package net.ilexiconn.pixle.level;

import net.darkhax.opennbt.tags.CompoundTag;
import net.darkhax.opennbt.tags.Tag;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.EntityRegistry;
import net.ilexiconn.pixle.level.generator.DefaultLevelGenerator;
import net.ilexiconn.pixle.level.generator.ILevelGenerator;
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

    public void setPixel(int pixel, int x, int y, PixelLayer layer) {
        Region regionForPixel = getRegionForPixel(x);
        x = x & (Region.REGION_WIDTH - 1);
        regionForPixel.setPixel(pixel, x, y, layer);
    }

    public int getPixel(int x, int y, PixelLayer layer) {
        Region region = getRegionForPixel(x);
        x = x & (Region.REGION_WIDTH - 1);
        return region.getPixel(x, y, layer);
    }

    public boolean hasPixel(int x, int y, PixelLayer layer) {
        return getPixel(x, y, layer) != 0;
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
            setRegion(region, x);
            region.generate(seed);
        }
        return region;
    }

    public int getHeight(int x, PixelLayer layer) {
        Region region = getRegionForPixel(x);
        x = x & (Region.REGION_WIDTH - 1);
        return region.getHeight(x, layer);
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
        entities.forEach(Entity::update);
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
        List<Bounds> colliding = new ArrayList<>();
        for (int y = (int) bounds.getMinY() - 1; y < Math.ceil(bounds.getMaxY()); y++) {
            for (int x = (int) bounds.getMinX() - 1; x < Math.ceil(bounds.getMaxX()); x++) {
                if (hasPixel(x, y, PixelLayer.FOREGROUND)) {
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
        entities.forEach(entity -> {
            CompoundTag compoundTag = new CompoundTag("");
            entity.writeToNBT(compoundTag);
            tagList.add(compoundTag);
        });
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
        tagList.forEach(tag -> {
            CompoundTag compoundTag = (CompoundTag) tag;
            Entity entity = EntityRegistry.initializeEntity(compoundTag.getByte("id"), this);
            if (entity != null) {
                entity.readFromNBT(compoundTag);
                addEntity(entity);
            }
        });
        tagList = compound.getTagList("regions");
        tagList.forEach(tag -> {
            CompoundTag compoundTag = (CompoundTag) tag;
            int regionX = compoundTag.getInt("regionX");
            Region region = new Region(regionX, this);
            region.readFromNBT(compoundTag);
            regions[regionX] = region;
        });
    }
}
