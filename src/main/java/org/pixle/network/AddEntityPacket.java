package org.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import org.pixle.client.PixleClient;
import org.pixle.entity.Entity;
import org.pixle.entity.EntityRegistry;
import org.pixle.entity.PlayerEntity;
import org.pixle.level.ClientLevel;
import org.pixle.server.PixleServer;

public class AddEntityPacket extends PixlePacket {
    private int entityInWorldID;
    private byte entityID;
    private double posX;
    private double posY;
    private String entityData;

    public AddEntityPacket() {
    }

    public AddEntityPacket(Entity entity) {
        this.entityInWorldID = entity.getEntityID();
        this.entityID = (byte) EntityRegistry.getEntityID(entity.getClass());
        this.posX = entity.posX;
        this.posY = entity.posY;
        this.entityData = entity.writeData();
    }

    @Override
    public void handleServer(PixleServer server, PlayerEntity player, Connection connection, long estimatedSendTime) {

    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
        ClientLevel level = client.getLevel();
        Entity entity = EntityRegistry.initializeEntity(entityID, null);
        if (entity != null) {
            entity.level = level;
            entity.entityID = entityInWorldID;
            entity.posX = posX;
            entity.posY = posY;
            entity.readData(entityData);
            level.addEntity(entity, false);
        }
    }
}
