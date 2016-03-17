package org.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import org.pixle.client.PixleClient;
import org.pixle.entity.PlayerEntity;
import org.pixle.server.PixleServer;

public class RequestRegionPacket extends PixlePacket {
    private int x;
    private byte y;
    private byte layer;

    public RequestRegionPacket() {
    }

    public RequestRegionPacket(int x, int y, int layer) {
        this.x = x;
        this.y = (byte) (y + Byte.MIN_VALUE);
        this.layer = (byte) layer;
    }

    @Override
    public void handleServer(PixleServer pixleServer, PlayerEntity player, Connection connection, long estimatedSendTime) {
        Server server = (Server) connection.getEndPoint();
        server.sendToTCP(connection.getID(), new SendRegionPacket(pixleServer.getLevel().getRegion(x, y - Byte.MIN_VALUE), layer));
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
    }
}
