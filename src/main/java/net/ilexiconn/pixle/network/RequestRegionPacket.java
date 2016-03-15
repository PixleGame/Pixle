package net.ilexiconn.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.server.PixleServer;

public class RequestRegionPacket extends PixlePacket {
    private int x;
    private byte ySection;
    private byte layer;

    public RequestRegionPacket() {
    }

    public RequestRegionPacket(int x, int ySection, PixelLayer layer) {
        this.x = x;
        this.ySection = (byte) ySection;
        this.layer = (byte) layer.ordinal();
    }

    @Override
    public void handleServer(PixleServer pixleServer, PlayerEntity player, Connection connection, long estimatedSendTime) {
        Server server = (Server) connection.getEndPoint();
        server.sendToTCP(connection.getID(), new SendRegionPacket(pixleServer.getLevel().getRegion(x), layer, ySection));
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
    }
}
