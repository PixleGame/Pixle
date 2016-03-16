package org.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import org.pixle.client.PixleClient;
import org.pixle.entity.PlayerEntity;
import org.pixle.server.PixleServer;

public class PlayerMovePacket extends PixlePacket {
    private float moveX;
    private boolean jumping;
    private int entityId;

    public PlayerMovePacket() {
    }

    public PlayerMovePacket(PlayerEntity player, float moveX, boolean jumping) {
        this.entityId = player.getEntityID();
        this.moveX = moveX;
        this.jumping = jumping;
    }

    @Override
    public void handleServer(PixleServer pixleServer, PlayerEntity player, Connection connection, long estimatedSendTime) {
        handle((PlayerEntity) pixleServer.getLevel().getEntityById(entityId), estimatedSendTime, false);
        Server server = (Server) connection.getEndPoint();
        server.sendToAllTCP(this);
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
        handle((PlayerEntity) client.getLevel().getEntityById(entityId), estimatedSendTime, true);
    }

    private void handle(PlayerEntity player, long estimatedSendTime, boolean client) {
        if (player != null) {
            player.moveX = moveX;
            player.jumping = jumping;
            int timeDifference = (int) (System.currentTimeMillis() - estimatedSendTime);
            if (client) {
                timeDifference *= 2;
            }
            for (int i = 0; i < (int) (timeDifference * 0.06F); i++) {
                player.updateMovement();
            }
        }
    }

}
