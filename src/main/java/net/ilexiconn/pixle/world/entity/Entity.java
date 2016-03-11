package net.ilexiconn.pixle.world.entity;

import net.darkhax.opennbt.tags.CompoundTag;
import net.ilexiconn.pixle.world.World;
import net.ilexiconn.pixle.world.bounds.Bounds;
import net.ilexiconn.pixle.world.bounds.EntityBounds;

import java.util.List;

public class Entity {
    public World world;
    public double posX;
    public double posY;
    public float velX;
    public float velY;

    public EntityBounds bounds;

    public boolean collidingVertically;
    public boolean collidingHorizontally;

    public boolean onSurface;

    public Entity(World world) {
        this.world = world;
        this.setBounds(1.0F, 2.0F);
    }

    public void setBounds(float sizeX, float sizeY) {
        this.bounds = new EntityBounds(this, sizeX, sizeY);
    }

    public void update() {
        velY -= getGravity() * 0.1F;

        if (velY < -1.0F) {
            velY = -1.0F;
        }

        move(velX, velY);

        float airFriction = getAirFriction();

        velX *= airFriction;
        velY *= airFriction;

        if (onSurface) {
//            velX *= standingOn.getMaterial().getFriction();
        }
    }

    public void move(float velX, float velY) {
        posX += velX;
        posY += velY;

        double originalX = posX;
        double originalY = posY;

        List<Bounds> intersectingBounds = world.getIntersectingPixelBounds(bounds);

        for (Bounds intersecting : intersectingBounds) {
            posX = bounds.preventIntersectionX(posX, intersecting);
            posY = bounds.preventIntersectionY(posY, intersecting);
        }

        collidingHorizontally = originalX != posX;
        collidingVertically = originalY != posY;
        onSurface = collidingVertically && posY > originalY;
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
