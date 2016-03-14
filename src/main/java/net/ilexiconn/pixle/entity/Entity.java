package net.ilexiconn.pixle.entity;

import net.darkhax.opennbt.tags.CompoundTag;
import net.ilexiconn.netconn.ByteBuffer;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.network.EntityPositionUpdatePacket;
import net.ilexiconn.pixle.server.PixleServer;
import net.ilexiconn.pixle.util.Bounds;
import net.ilexiconn.pixle.util.EntityBounds;

import java.util.List;

public abstract class Entity {
    public Level level;
    public double posX;
    public double posY;
    public double prevPosX;
    public double prevPosY;
    public float velX;
    public float velY;

    public int entityID;

    public EntityBounds bounds;

    public boolean collidingVertically;
    public boolean collidingHorizontally;

    public boolean onSurface;

    public int ticks;

    public Entity(Level level) {
        this.level = level;
        this.setBounds(1.0F, 2.0F);
    }

    public void setBounds(float sizeX, float sizeY) {
        this.bounds = new EntityBounds(this, sizeX, sizeY);
    }

    public void update() {
        ticks++;

        updateMovement();

        if (level.getSide().isServer() && ticks % 4 == 0) {
            if (posX != prevPosX || posY != prevPosY) {
                PixleServer.INSTANCE.getServer().sendPacketToAllClients(new EntityPositionUpdatePacket(this));
            }
            prevPosX = posX;
            prevPosY = posY;
        }
    }

    public void updateMovement() {
        velY -= getGravity() * 0.1F;

        if (velY < -1.0F) {
            velY = -1.0F;
        }

        move(velX, velY);

        float airFriction = getAirFriction();

        velX *= airFriction;
        velY *= airFriction;
    }

    public void move(float velX, float velY) {
        posX += velX;
        posY += velY;

        double originalX = posX;
        double originalY = posY;

        List<Bounds> intersectingBounds = level.getIntersectingPixelBounds(bounds);

        for (Bounds intersecting : intersectingBounds) {
            posY = bounds.preventIntersectionY(posY, intersecting);
            posX = bounds.preventIntersectionX(posX, intersecting);
        }

        collidingHorizontally = originalX != posX;
        collidingVertically = originalY != posY;
        onSurface = collidingVertically && posY > originalY;
    }

    public float getGravity() {
        return 0.5F;
    }

    public float getAirFriction() {
        return 0.9F;
    }

    public void writeToNBT(CompoundTag compound) {
        compound.setInt("entityID", entityID);
        compound.setByte("id", (byte) EntityRegistry.getEntityID(getClass()));
        compound.setDouble("posX", posX);
        compound.setDouble("posY", posY);
        compound.setFloat("velX", velX);
        compound.setFloat("velY", velY);
    }

    public void readFromNBT(CompoundTag compound) {
        this.entityID = compound.getInt("entityID");
        this.posX = compound.getDouble("posX");
        this.posY = compound.getDouble("posY");
        this.velX = compound.getFloat("velX");
        this.velY = compound.getFloat("velY");
    }

    public void destroy() {
        level.removeEntity(this);
    }

    public int getEntityID() {
        return entityID;
    }

    public abstract void writeData(ByteBuffer buffer);
    public abstract void readData(ByteBuffer buffer);

    @Override
    public boolean equals(Object o) {
        if (o instanceof Entity) {
            Entity entity = (Entity) o;
            return entity.entityID == entityID;
        }
        return false;
    }
}
