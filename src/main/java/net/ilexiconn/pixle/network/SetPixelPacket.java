package net.ilexiconn.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.server.PixleServer;

public class SetPixelPacket extends PixlePacket {
    private int pixel;
    private int x;
    private int y;
    private byte layer;

    public SetPixelPacket() {
    }

    public SetPixelPacket(int pixel, int x, int y, PixelLayer layer) {
        this.pixel = pixel;
        this.x = x;
        this.y = y;
        this.layer = (byte) layer.ordinal();
    }

    @Override
    public void handleServer(PixleServer pixleServer, PlayerEntity player, Connection connection, long estimatedSendTime) {

    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
        client.getLevel().setPixel(pixel, x, y, PixelLayer.values()[layer]);
    }
}
