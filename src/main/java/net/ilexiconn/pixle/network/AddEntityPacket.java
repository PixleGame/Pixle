package net.ilexiconn.pixle.network;

import net.ilexiconn.netconn.ByteBuffer;
import net.ilexiconn.netconn.INetworkManager;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.EntityRegistry;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.ClientLevel;
import net.ilexiconn.pixle.server.PixleServer;

import java.net.Socket;

public class AddEntityPacket extends PixlePacket {
    private int entityId;
    private Entity entity;

    public AddEntityPacket() {}

    public AddEntityPacket(Entity entity) {
        this.entity = entity;
        this.entityId = entity.getEntityId();
    }

    @Override
    public void handleServer(PixleServer server, Socket sender, PlayerEntity player, INetworkManager networkManager, long estimatedSendTime) {
    }

    @Override
    public void handleClient(PixleClient client, INetworkManager networkManager, long estimatedSendTime) {
        ClientLevel level = client.getLevel();
        if (entity != null) {
            entity.level = level;
            entity.entityId = entityId;
            level.addEntity(entity, false);
        }
    }

    @Override
    public void encode(ByteBuffer buffer) {
        buffer.writeInteger(entityId);
        buffer.writeByte((byte) EntityRegistry.getEntityID(entity.getClass()));
        buffer.writeDouble(entity.posX);
        buffer.writeDouble(entity.posY);
        entity.writeData(buffer);
    }

    @Override
    public void decode(ByteBuffer buffer) {
        entityId = buffer.readInteger();
        byte entityTypeId = buffer.readByte();
        entity = EntityRegistry.initializeEntity(entityTypeId, null);
        if (entity != null) {
            entity.posX = buffer.readDouble();
            entity.posY = buffer.readDouble();
            entity.readData(buffer);
        }
    }
}
