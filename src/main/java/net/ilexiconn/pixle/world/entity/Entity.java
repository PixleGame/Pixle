package net.ilexiconn.pixle.world.entity;

import net.darkhax.opennbt.tags.CompoundTag;
import net.ilexiconn.pixle.world.World;
import net.ilexiconn.pixle.world.pixel.Pixel;

public class Entity {
    public World world;
    public double posX;
    public double posY;
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

        move(velX, velY);

        float airFriction = getAirFriction();

        velX *= airFriction;
        velY *= airFriction;

        if (onSurface) {
            velX *= standingOn.getMaterial().getFriction();
        }
    }

    public void move(float velX, float velY) {
        posX += velX;
        posY += velY;

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
        compound.setDouble("posX", posX);
        compound.setDouble("posY", posY);
        compound.setFloat("velX", velX);
        compound.setFloat("velY", velY);
    }

    public void readFromNBT(CompoundTag compound) {
        this.posX = compound.getDouble("posX");
        this.posY = compound.getDouble("posY");
        this.velX = compound.getFloat("velX");
        this.velY = compound.getFloat("velY");
    }

    public void destroy() {
        world.removeEntity(this);
    }
}
