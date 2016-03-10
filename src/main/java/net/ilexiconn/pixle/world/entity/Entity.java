package net.ilexiconn.pixle.world.entity;

import net.darkhax.opennbt.tags.CompoundTag;
import net.ilexiconn.pixle.world.World;
import net.ilexiconn.pixle.world.pixel.Pixel;

public class Entity {
    public World world;
    public float x;
    public float y;
    public float velX;
    public float velY;
    public boolean onSurface;
    public Pixel standingOn;

    public Entity(World world) {
        this.world = world;
    }

    public void update() {
        velY -= getGravity() * 0.1F;

        if (velY < -1.0F) {
            velY = 1.0F;
        }

        float airFriction = getAirFriction();

        velX *= airFriction;
        velY *= airFriction;

        if (onSurface) {
            velX *= standingOn.getMaterial().getFriction();
        }
    }

    public void move(float velX, float velY) {
        x += velX;
        y += velY;

        //TODO check for collision
    }

    public float getGravity() {
        return 1.0F;
    }

    public float getAirFriction() {
        return 0.9F;
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

    public void destroy() {
        world.removeEntity(this);
    }
}
