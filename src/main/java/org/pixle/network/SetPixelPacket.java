package org.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import org.pixle.client.PixleClient;
import org.pixle.entity.PlayerEntity;
import org.pixle.level.PixelLayer;
import org.pixle.pixel.Pixel;
import org.pixle.server.PixleServer;

public class SetPixelPacket extends PixlePacket {
    private byte pixel;
    private int x;
    private int y;
    private byte layer;

    public SetPixelPacket() {
    }

    public SetPixelPacket(int pixel, int x, int y, PixelLayer layer) {
        this.pixel = (byte) pixel;
        this.x = x;
        this.y = y;
        this.layer = (byte) layer.ordinal();
    }

    @Override
    public void handleServer(PixleServer server, PlayerEntity player, Connection connection, long estimatedSendTime) {
        int distX = (int) (x - player.posX);
        int distY = (int) (y - player.posY);
        double dist = Math.sqrt(distX * distX + distY * distY);
        if (dist < PlayerEntity.REACH_DISTANCE) {
            server.getLevel().setPixel(Pixel.getPixelByID(pixel), x, y, PixelLayer.values()[layer]);
            server.getServer().sendToAllTCP(this);
        }
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
        client.getLevel().setPixel(Pixel.getPixelByID(pixel), x, y, PixelLayer.values()[layer]);
    }
}
