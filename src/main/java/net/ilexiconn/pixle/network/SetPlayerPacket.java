package net.ilexiconn.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.server.PixleServer;

public class SetPlayerPacket extends PixlePacket {
    private int playerId;

    public SetPlayerPacket() {
    }

    public SetPlayerPacket(PlayerEntity entity) {
        playerId = entity.getEntityID();
    }

    @Override
    public void handleServer(PixleServer pixleServer, PlayerEntity player, Connection connection, long estimatedSendTime) {
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
        client.setPlayer(playerId);
    }
}
