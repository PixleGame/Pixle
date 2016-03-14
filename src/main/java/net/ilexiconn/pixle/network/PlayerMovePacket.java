package net.ilexiconn.pixle.network;

import net.ilexiconn.netconn.ByteBuffer;
import net.ilexiconn.netconn.INetworkManager;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.server.PixleServer;

import java.net.Socket;

public class PlayerMovePacket extends PixlePacket {
    private float moveX;
    private boolean jumping;
    private int entityId;

    public PlayerMovePacket() {}

    public PlayerMovePacket(PlayerEntity player, float moveX, boolean jumping) {
        this.entityId = player.getEntityId();
        this.moveX = moveX;
        this.jumping = jumping;
    }

    @Override
    public void handleServer(PixleServer server, Socket sender, PlayerEntity player, INetworkManager networkManager) {
        handle((PlayerEntity) server.getLevel().getEntityById(entityId));
        networkManager.sendPacketToAllClients(this);
    }

    @Override
    public void handleClient(PixleClient client, INetworkManager networkManager) {
        handle((PlayerEntity) client.getLevel().getEntityById(entityId));
    }

    private void handle(PlayerEntity player) {
        player.velX = moveX;
        if (player.onSurface && jumping) {
            player.velY = 1.0F;
        }
    }

    @Override
    public void encode(ByteBuffer buffer) {
        buffer.writeInteger(entityId);
        buffer.writeFloat(moveX);
        buffer.writeByte((byte) (jumping ? 1 : 0));
    }

    @Override
    public void decode(ByteBuffer buffer) {
        entityId = buffer.readInteger();
        moveX = buffer.readFloat();
        jumping = buffer.readByte() == 1;
    }
}
