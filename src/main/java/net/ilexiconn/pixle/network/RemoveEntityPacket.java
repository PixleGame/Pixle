package net.ilexiconn.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.ClientLevel;
import net.ilexiconn.pixle.server.PixleServer;

public class RemoveEntityPacket extends PixlePacket {
    private int entityId;

    public RemoveEntityPacket() {}

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
