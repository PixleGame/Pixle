package net.ilexiconn.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.server.PixleServer;

public class RequestRegionPacket extends PixlePacket {
    private int x;

    public RequestRegionPacket() {
    }

    public RequestRegionPacket(int x) {
        this.x = x;
    }

    @Override
    public void handleServer(PixleServer pixleServer, PlayerEntity player, Connection connection, long estimatedSendTime) {
        Server server = (Server) connection.getEndPoint();
        Level level = pixleServer.getLevel();
        Region region = level.getRegion(x);
        for (PixelLayer layer : PixelLayer.values()) {
            server.sendToTCP(connection.getID(), new SendRegionPacket(region, layer));
        }
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
    }
}
