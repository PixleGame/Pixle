package net.ilexiconn.pixle.network;

import net.ilexiconn.netconn.ByteBuffer;
import net.ilexiconn.netconn.INetworkManager;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.ClientLevel;
import net.ilexiconn.pixle.server.PixleServer;

import java.net.Socket;

public class RemoveEntityPacket extends PixlePacket {
    private int entityId;

    public RemoveEntityPacket() {}

    public RemoveEntityPacket(Entity entity) {
        entityId = entity.getEntityID();
    }

    @Override
    public void handleServer(PixleServer server, Socket sender, PlayerEntity player, INetworkManager networkManager, long estimatedSendTime) {
    }

    @Override
    public void handleClient(PixleClient client, INetworkManager networkManager, long estimatedSendTime) {
        ClientLevel level = client.getLevel();
        level.removeEntity(level.getEntityById(entityId));
    }

    @Override
    public void encode(ByteBuffer buffer) {
        buffer.writeInteger(entityId);
    }

    @Override
    public void decode(ByteBuffer buffer) {
        entityId = buffer.readInteger();
    }
}
