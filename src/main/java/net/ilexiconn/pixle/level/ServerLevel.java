package net.ilexiconn.pixle.level;

import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.network.AddEntityPacket;
import net.ilexiconn.pixle.network.RemoveEntityPacket;
import net.ilexiconn.pixle.network.SetPixelPacket;
import net.ilexiconn.pixle.server.PixleServer;
import net.ilexiconn.pixle.util.Side;

public class ServerLevel extends Level {
    @Override
    public Side getSide() {
        return Side.SERVER;
    }

    @Override
    public void setPixel(int pixel, int x, int y, PixelLayer layer) {
        super.setPixel(pixel, x, y, layer);
        PixleServer.INSTANCE.getServer().sendPacketToAllClients(new SetPixelPacket(pixel, x, y, layer));
    }

    @Override
    public boolean addEntity(Entity entity, boolean assignId) {
        if (assignId) {
            entity.entityId = getUniqueEntityId();
        }
        boolean add = super.addEntity(entity, assignId);
        if (add) {
            PixleServer.INSTANCE.getServer().sendPacketToAllClients(new AddEntityPacket(entity));
        }
        return add;
    }

    @Override
    public void removeEntity(Entity entity) {
        super.removeEntity(entity);
        if (entity != null) {
            PixleServer.INSTANCE.getServer().sendPacketToAllClients(new RemoveEntityPacket(entity));
        }
    }

    @Override
    public void requestRegion(Region region, int regionX) {
        region.generate(seed);
    }
}