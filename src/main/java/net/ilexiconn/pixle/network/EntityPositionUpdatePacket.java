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
    private float velX;
    private float velY;

    public EntityPositionUpdatePacket() {}

    public EntityPositionUpdatePacket(Entity entity) {
        entityId = entity.getEntityID();
        posX = entity.posX;
        posY = entity.posY;
        velX = entity.velX;
        velY = entity.velY;
    }

    @Override
    public void handleServer(PixleServer server, Socket sender, PlayerEntity player, INetworkManager networkManager, long estimatedSendTime) {
    }

    @Override
    public void handleClient(PixleClient client, INetworkManager networkManager, long estimatedSendTime) {
        Level level = client.getLevel();
        Entity entity = level.getEntityById(entityId);
        int timeDifference = (int) (System.currentTimeMillis() - estimatedSendTime);
        if (entity != null) {
            entity.prevPosX = entity.posX;
            entity.prevPosY = entity.posY;
            entity.posX = posX;
            entity.posY = posY;
            entity.velX = velX;
            entity.velY = velY;
            for (int i = 0; i < (int) (timeDifference * 0.06F); i++) {
                entity.updateMovement();
            }
        }
    }

    @Override
    public void encode(ByteBuffer buffer) {
        buffer.writeInteger(entityId);
        buffer.writeDouble(posX);
        buffer.writeDouble(posY);
        buffer.writeFloat(velX);
        buffer.writeFloat(velY);
    }

    @Override
    public void decode(ByteBuffer buffer) {
        entityId = buffer.readInteger();
        posX = buffer.readDouble();
        posY = buffer.readDouble();
        velX = buffer.readFloat();
        velY = buffer.readFloat();
    }
}
