package net.ilexiconn.pixle.level.region;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.level.ClientLevel;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.network.RequestRegionPacket;

public class ReceivingRegion {
    private int x;
    private int[][][] pixels = new int[PixelLayer.values().length][Region.REGION_WIDTH][Region.REGION_HEIGHT];
    private byte currentLayer;
    private byte currentYSection;

    public ReceivingRegion(int x) {
        this.x = x;
    }

    public void receivePart(ClientLevel level, byte layer, byte ySection, byte[][] pixels) {
        int yOffset = ySection * 16;
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < 16; y++) {
                this.pixels[layer][x][y + yOffset] = pixels[x][y];
            }
        }
        boolean complete = false;
        if (currentYSection < (Region.REGION_HEIGHT / 16) - 1) {
            currentYSection++;
        } else {
            if (currentLayer < PixelLayer.values().length - 1) {
                currentLayer++;
            } else {
                Region region = level.getRegion(x);
                region.setPixels(this.pixels);
                level.completeRegion(this);
                complete = true;
            }
        }
        if (!complete) {
            requestNext();
        }
    }

    public void requestNext() {
        PixleClient.INSTANCE.getClient().sendTCP(new RequestRegionPacket(x, currentYSection, PixelLayer.values()[currentLayer]));
    }

    public int getX() {
        return x;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ReceivingRegion) {
            return ((ReceivingRegion) o).x == x;
        }
        return false;
    }
}
