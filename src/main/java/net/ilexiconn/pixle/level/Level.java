package net.ilexiconn.pixle.level;

import net.darkhax.opennbt.tags.CompoundTag;
import net.darkhax.opennbt.tags.Tag;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.EntityRegistry;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.generator.DefaultLevelGenerator;
import net.ilexiconn.pixle.level.generator.ILevelGenerator;
import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.pixel.Pixel;
import net.ilexiconn.pixle.util.Bounds;
import net.ilexiconn.pixle.util.PixelBounds;
import net.ilexiconn.pixle.util.Side;

import java.util.ArrayList;
import java.util.List;

public abstract class Level {
    protected static final int LEVEL_REGION_WIDTH = 62500;

    protected Region[][] regions = new Region[LEVEL_REGION_WIDTH][getRegionY(LEVEL_HEIGHT)];

    protected ILevelGenerator levelGenerator;
    protected long seed;

    protected List<Entity> entities = new ArrayList<>();
    protected List<PlayerEntity> players = new ArrayList<>();

    public static final int PIXEL_SIZE = 6;

    public static int nextEntityId;

    public static final int LEVEL_HEIGHT = 1024;

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

    public void setPixel(Pixel pixel, int x, int y, PixelLayer layer) {
        if (y >= 0 && y < LEVEL_HEIGHT) {
            Region regionForPixel = getRegionForPixel(x, y);
            x = x & (Region.REGION_WIDTH - 1);
            y = y & (Region.REGION_HEIGHT - 1);
            regionForPixel.setPixel(pixel, x, y, layer);
        }
    }

    public Pixel getPixel(int x, int y, PixelLayer layer) {
        if (y >= 0 && y < LEVEL_HEIGHT) {
            Region region = getRegionForPixel(x, y);
            x = x & (Region.REGION_WIDTH - 1);
            y = y & (Region.REGION_HEIGHT - 1);
            return region.getPixel(x, y, layer);
        }
        return Pixel.air;
    }

    public boolean hasPixel(int x, int y, PixelLayer layer) {
        return getPixel(x, y, layer) != Pixel.air;
    }

    public int getRegionX(int x) {
        return x >> 5;
    }

    public Region getRegionForPixel(int x, int y) {
        return getRegion(getRegionX(x), getRegionY(y));
    }

    private int getRegionY(int y) {
        return y / Region.REGION_HEIGHT;
    }

    public Region getRegion(int x, int y) {
        Region region = regions[getRegionXIndex(x)][y];
        if (region == null) {
            region = new Region(x, y, this);
            setRegion(region, x, y);
            requestRegion(region, x, y);
        }
        return region;
    }

    public int getHeight(int x, PixelLayer layer) {
        for (int y = LEVEL_HEIGHT - 1; y >= 0; y--) {
            if (hasPixel(x, y, layer)) {
                return y;
            }
        }
        return 0;
    }

    public void setRegion(Region region, int x, int y) {
        regions[getRegionXIndex(x)][y] = region;
    }

    protected int getRegionXIndex(int x) {
        return x + (LEVEL_REGION_WIDTH / 2);
    }

    public ILevelGenerator getLevelGenerator() {
        return levelGenerator;
    }

    public void update() {
        for (Entity entity : getEntities()) {
            entity.update();
        }
    }

    public boolean addEntity(Entity entity, boolean assignId) {
        if (entity != null) {
            if (!getEntities().contains(entity)) {
                entities.add(entity);
                if (entity instanceof PlayerEntity) {
                    players.add((PlayerEntity) entity);
                }
                return true;
            }
        }
        return false;
    }

    public Entity getEntityById(int entityId) {
        for (Entity entity : getEntities()) {
            if (entity.entityID == entityId) {
                return entity;
            }
        }
        return null;
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
        if (entity instanceof PlayerEntity) {
            players.remove(entity);
        }
    }

    public List<Entity> getEntities() {
        return new ArrayList<>(entities);
    }

    public List<PlayerEntity> getPlayers() {
        return new ArrayList<>(players);
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
        for (Region[] regionX : regions) {
            for (Region region : regionX) {
                if (region != null) {
                    CompoundTag compoundTag = new CompoundTag("");
                    region.writeToNBT(compoundTag);
                    tagList.add(compoundTag);
                }
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
                addEntity(entity, false);
            }
        });
        tagList = compound.getTagList("regions");
        tagList.forEach(tag -> {
            CompoundTag compoundTag = (CompoundTag) tag;
            int regionX = compoundTag.getInt("regionX");
            int regionY = compoundTag.getByte("regionY");
            Region region = new Region(regionX, regionY, this);
            region.readFromNBT(compoundTag);
            regions[regionX][regionY] = region;
        });
    }

    protected int getUniqueEntityId() {
        int id = Level.nextEntityId;
        Level.nextEntityId++;
        return id;
    }

    public PlayerEntity getPlayerByUsername(String username) {
        for (PlayerEntity player : getPlayers()) {
            if (player.username.equals(username)) {
                return player;
            }
        }
        return null;
    }

    public abstract Side getSide();

    public abstract void requestRegion(Region region, int regionX, int regionY);
}
