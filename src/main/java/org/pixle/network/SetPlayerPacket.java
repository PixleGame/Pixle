package org.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import org.pixle.client.PixleClient;
import org.pixle.entity.PlayerEntity;
import org.pixle.server.PixleServer;

public class SetPlayerPacket extends PixlePacket {
    private int playerId;

    public SetPlayerPacket() {
    }

    public SetPlayerPacket(PlayerEntity entity) {
        playerId = entity.getEntityID();
    }

    @Override
    public void handleServer(PixleServer server, PlayerEntity player, Connection connection, long estimatedSendTime) {
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
        client.setPlayer(playerId);
    }
}
