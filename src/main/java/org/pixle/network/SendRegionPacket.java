package org.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import org.pixle.client.PixleClient;
import org.pixle.entity.PlayerEntity;
import org.pixle.level.ClientLevel;
import org.pixle.level.PixelLayer;
import org.pixle.level.region.Region;
import org.pixle.server.PixleServer;

public class SendRegionPacket extends PixlePacket {
    private byte layer;
    private int regionX;
    private byte regionY;
    private byte[][] pixels;
    private boolean empty;

    public SendRegionPacket() {
    }

    public SendRegionPacket(Region region, byte layer) {
        this.layer = layer;
        this.regionX = region.getX();
        this.regionY = (byte) (region.getY() + Byte.MIN_VALUE);
        this.empty = region.isEmpty(PixelLayer.values()[layer]);

        if (!this.empty) {
            pixels = new byte[Region.REGION_WIDTH][Region.REGION_HEIGHT];
            int[][] regionPixels = region.getPixels()[layer];

            for (int x = 0; x < pixels.length; x++) {
                for (int y = 0; y < pixels[0].length; y++) {
                    pixels[x][y] = (byte) regionPixels[x][y];
                }
            }
        }
    }

    @Override
    public void handleServer(PixleServer server, PlayerEntity player, Connection connection, long estimatedSendTime) {
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
        ClientLevel level = client.getLevel();
        Region region = level.getRegion(regionX, regionY - Byte.MIN_VALUE);
        if (!this.empty) {
            region.setPixels(pixels, layer);
        }
        region.setLoaded();
    }
}
