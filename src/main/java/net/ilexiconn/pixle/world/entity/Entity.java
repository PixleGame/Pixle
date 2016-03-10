package net.ilexiconn.pixle.world.entity;

import net.darkhax.opennbt.tags.CompoundTag;
import net.ilexiconn.pixle.world.World;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    public World world;
    public float x;
    public float y;
    public float velX;
    public float velY;

    public Entity(World world) {
        this.world = world;
    }

    public void writeToNBT(CompoundTag compound) {
        compound.setByte("id", (byte) EntityRegistry.getEntityID(getClass()));
        compound.setFloat("x", x);
        compound.setFloat("y", y);
        compound.setFloat("velX", velX);
        compound.setFloat("velY", velY);
    }

    public void readFromNBT(CompoundTag compound) {
        this.x = compound.getFloat("x");
        this.y = compound.getFloat("y");
        this.velX = compound.getFloat("velX");
        this.velY = compound.getFloat("velY");
    }
}
