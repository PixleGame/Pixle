package net.ilexiconn.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.ClientLevel;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.server.PixleServer;

public class SendRegionPacket extends PixlePacket {
    private byte layer;
    private byte ySection;
    private int regionX;
    private byte[][] pixels;

    public SendRegionPacket() {
    }

    public SendRegionPacket(Region region, PixelLayer layer, int ySection) {
        this.layer = (byte) layer.ordinal();
        this.regionX = region.getX();
        this.ySection = (byte) ySection;

        pixels = new byte[Region.REGION_WIDTH][Region.REGION_HEIGHT];
        int[][] regionPixels = region.getPixels()[layer.ordinal()];

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
        PixelLayer layer = PixelLayer.values()[this.layer];

        int width = Region.REGION_WIDTH;

        int[][] pixels = new int[width][16];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < 16; y++) {
                pixels[x][y] = this.pixels[x][y];
            }
        }

        int yOffset = ySection * 16;

        ClientLevel level = client.getLevel();
        Region region = level.getRegion(regionX);
        region.setPixels(pixels, layer, yOffset);
        level.receiveRegion(region);
    }
}
