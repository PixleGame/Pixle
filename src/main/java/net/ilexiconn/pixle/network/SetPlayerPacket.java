package net.ilexiconn.pixle.network;

import net.ilexiconn.netconn.ByteBuffer;
import net.ilexiconn.netconn.INetworkManager;
import net.ilexiconn.netconn.IPacket;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.server.PixleServer;

import java.net.Socket;

public class SetPlayerPacket extends PixlePacket {
    private int playerId;

    public SetPlayerPacket() {}

    public SetPlayerPacket(PlayerEntity entity) {
        playerId = entity.getEntityId();
    }

    @Override
    public void encode(ByteBuffer buffer) {
        buffer.writeInteger(playerId);
    }

    @Override
    public void decode(ByteBuffer buffer) {
        playerId = buffer.readInteger();
    }

    @Override
    public void handleServer(PixleServer server, Socket sender, PlayerEntity player, INetworkManager networkManager) {
    }

    @Override
    public void handleClient(PixleClient client, INetworkManager networkManager) {
        client.setPlayer(playerId);
    }
}
