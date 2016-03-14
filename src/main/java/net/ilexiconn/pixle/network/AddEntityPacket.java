package net.ilexiconn.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.EntityRegistry;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.ClientLevel;
import net.ilexiconn.pixle.server.PixleServer;

import java.net.Socket;

public class AddEntityPacket extends PixlePacket {
    private int entityInWorldID;
    private byte entityID;
    private double posX;
    private double posY;
    private String entityData;

    public AddEntityPacket() {}

    public AddEntityPacket(Entity entity) {
        this.entityInWorldID = entity.getEntityID();
        this.entityID = (byte) EntityRegistry.getEntityID(entity.getClass());
        this.posX = entity.posX;
        this.posY = entity.posY;
        this.entityData = entity.writeData();
    }

    @Override
    public void handleServer(PixleServer pixleServer, PlayerEntity player, Connection connection, long estimatedSendTime) {

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
