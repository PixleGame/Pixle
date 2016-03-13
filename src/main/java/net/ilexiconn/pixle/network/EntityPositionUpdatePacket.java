package net.ilexiconn.pixle.network;

import net.ilexiconn.netconn.ByteBuffer;
import net.ilexiconn.netconn.INetworkManager;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.server.PixleServer;

import java.net.Socket;

public class EntityPositionUpdatePacket extends PixlePacket {
    private int entityId;
    private double posX;
    private double posY;

    public EntityPositionUpdatePacket() {}

    public EntityPositionUpdatePacket(Entity entity) {
        entityId = entity.getEntityId();
        posX = entity.posX;
        posY = entity.posY;
    }

    @Override
    public void handleServer(PixleServer server, Socket sender, PlayerEntity player, INetworkManager networkManager) {
    }

    @Override
    public void handleClient(PixleClient client, INetworkManager networkManager) {
        Level level = client.getLevel();
        Entity entity = level.getEntityById(entityId);
        if (entity != null) {
            entity.posX = posX;
            entity.posY = posY;
        }
    }

    @Override
    public void encode(ByteBuffer buffer) {
        buffer.writeInteger(entityId);
        buffer.writeDouble(posX);
        buffer.writeDouble(posY);
    }

    @Override
    public void decode(ByteBuffer buffer) {
        entityId = buffer.readInteger();
        posX = buffer.readDouble();
        posY = buffer.readDouble();
    }
}
