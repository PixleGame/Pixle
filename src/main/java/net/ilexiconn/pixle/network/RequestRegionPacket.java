package net.ilexiconn.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.server.PixleServer;

public class RequestRegionPacket extends PixlePacket {
    private int x;
    private byte y;
    private byte layer;

    public RequestRegionPacket() {
    }

    public RequestRegionPacket(int x, int y, int layer) {
        this.x = x;
        this.y = (byte) y;
        this.layer = (byte) layer;
    }

    @Override
    public void handleServer(PixleServer pixleServer, PlayerEntity player, Connection connection, long estimatedSendTime) {
        Server server = (Server) connection.getEndPoint();
        server.sendToTCP(connection.getID(), new SendRegionPacket(pixleServer.getLevel().getRegion(x, y), layer));
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
    }
}
