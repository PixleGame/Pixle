package net.ilexiconn.pixle.network;

import net.ilexiconn.netconn.ByteBuffer;
import net.ilexiconn.netconn.INetworkManager;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.server.PixleServer;

import java.net.Socket;

public class RequestRegionPacket extends PixlePacket {
    private int x;

    public RequestRegionPacket() {}

    public RequestRegionPacket(int x) {
        this.x = x;
    }

    @Override
    public void handleServer(PixleServer server, Socket sender, PlayerEntity player, INetworkManager networkManager) {
        Region region = server.getLevel().getRegion(x);
//        for (PixelLayer layer : PixelLayer.values()) {
//            networkManager.sendPacketToClient(new SendRegionPacket(region, PixelLayer.FOREGROUND), sender);
//        }
    }

    @Override
    public void handleClient(PixleClient client, INetworkManager networkManager) {
    }

    @Override
    public void encode(ByteBuffer buffer) {
        buffer.writeInteger(x);
    }

    @Override
    public void decode(ByteBuffer buffer) {
        x = buffer.readInteger();
    }
}
