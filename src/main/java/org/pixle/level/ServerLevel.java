package org.pixle.level;

import org.pixle.entity.Entity;
import org.pixle.level.region.Region;
import org.pixle.network.AddEntityPacket;
import org.pixle.network.RemoveEntityPacket;
import org.pixle.server.PixleServer;
import org.pixle.util.Side;

public class ServerLevel extends Level {
    @Override
    public Side getSide() {
        return Side.SERVER;
    }

    @Override
    public boolean addEntity(Entity entity, boolean assignId) {
        if (assignId) {
            entity.entityID = getUniqueEntityId();
        }
        boolean add = super.addEntity(entity, assignId);
        if (add) {
            PixleServer.INSTANCE.getServer().sendToAllTCP(new AddEntityPacket(entity));
        }
        return add;
    }

    @Override
    public void removeEntity(Entity entity) {
        super.removeEntity(entity);
        if (entity != null) {
            PixleServer.INSTANCE.getServer().sendToAllTCP(new RemoveEntityPacket(entity));
        }
    }

    @Override
    public void requestRegion(Region region, int regionX, int regionY) {
        region.generate(seed);
    }
}
