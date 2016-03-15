package net.ilexiconn.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.ClientLevel;
import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.server.PixleServer;

public class SendRegionPacket extends PixlePacket {
    private byte layer;
    private int regionX;
    private byte regionY;
    private byte[][] pixels;

    public SendRegionPacket() {
    }

    public SendRegionPacket(Region region, byte layer) {
        this.layer = layer;
        this.regionX = region.getX();
        this.regionY = (byte) region.getY();

        pixels = new byte[Region.REGION_WIDTH][Region.REGION_HEIGHT];
        int[][] regionPixels = region.getPixels()[layer];

        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++) {
                pixels[x][y] = (byte) regionPixels[x][y];
            }
        }
    }

    @Override
    public void handleServer(PixleServer pixleServer, PlayerEntity player, Connection connection, long estimatedSendTime) {
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
        ClientLevel level = client.getLevel();
        Region region = level.getRegion(regionX, regionY);
        region.setPixels(pixels, layer);
    }
}
