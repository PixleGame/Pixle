package net.ilexiconn.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.server.PixleServer;

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
    public void handleServer(PixleServer pixleServer, PlayerEntity player, Connection connection, long estimatedSendTime) {
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
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
}
