package net.ilexiconn.pixle.world.entity;

import net.darkhax.opennbt.tags.CompoundTag;
import net.ilexiconn.pixle.world.World;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    private static List<Class<? extends Entity>> entityList = new ArrayList<>();

    public World world;
    public float posX;
    public float posY;
    public float velX;
    public float velY;

    static {
        //register entities
    }

    public Entity(World world) {
        this.world = world;
    }

    public void writeToNBT(CompoundTag compound) {
        compound.setByte("id", (byte) getEntityID(getClass()));
        compound.setFloat("posX", posX);
        compound.setFloat("posY", posY);
        compound.setFloat("velX", velX);
        compound.setFloat("velY", velY);
    }

    public void readFromNBT(CompoundTag compound) {
        this.posX = compound.getFloat("posX");
        this.posY = compound.getFloat("posY");
        this.velX = compound.getFloat("velX");
        this.velY = compound.getFloat("velY");
    }

    public static Entity initializeEntity(int id, World world) {
        try {
            return entityList.get(id).getConstructor(World.class).newInstance(world);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getEntityID(Class<? extends Entity> entityClass) {
        for (int i = 0; i < entityList.size(); i++) {
            if (entityList.get(i) == entityClass) {
                return i;
            }
        }
        return -1;
    }
}
