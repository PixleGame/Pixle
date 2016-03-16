package org.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import org.pixle.client.PixleClient;
import org.pixle.entity.Entity;
import org.pixle.entity.PlayerEntity;
import org.pixle.level.ClientLevel;
import org.pixle.server.PixleServer;

public class RemoveEntityPacket extends PixlePacket {
    private int entityId;

    public RemoveEntityPacket() {
    }

    public RemoveEntityPacket(Entity entity) {
        entityId = entity.getEntityID();
    }

    @Override
    public void handleServer(PixleServer pixleServer, PlayerEntity player, Connection connection, long estimatedSendTime) {
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
        ClientLevel level = client.getLevel();
        level.removeEntity(level.getEntityById(entityId));
    }
}
