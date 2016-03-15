package net.ilexiconn.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.ClientLevel;
import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.server.PixleServer;

public class SendRegionPacket extends PixlePacket {
    private byte layer;
    private byte ySection;
    private int regionX;
    private byte[][] pixels;

    public SendRegionPacket() {
    }

    public SendRegionPacket(Region region, byte layer, int ySection) {
        this.layer = layer;
        this.regionX = region.getX();
        this.ySection = (byte) ySection;

        pixels = new byte[Region.REGION_WIDTH][16];
        int[][] regionPixels = region.getPixels()[layer];

        int yOffset = ySection * 16;

        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < 16; y++) {
                pixels[x][y] = (byte) regionPixels[x][y + yOffset];
            }
        }
    }

    @Override
    public void handleServer(PixleServer pixleServer, PlayerEntity player, Connection connection, long estimatedSendTime) {
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
        ClientLevel level = client.getLevel();
        level.getReceivingRegion(regionX).receivePart(level, layer, ySection, pixels);
    }
}
