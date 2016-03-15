package net.ilexiconn.pixle.level;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.network.RequestRegionPacket;
import net.ilexiconn.pixle.util.Side;

import java.util.ArrayList;
import java.util.List;

public class ClientLevel extends Level {
    private List<Position> requestingRegions = new ArrayList<>();

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void requestRegion(Region region, int regionX, int regionY) {
        Position position = new Position(regionX, regionY);
        if (!requestingRegions.contains(position)) {
            requestingRegions.add(position);
            for (PixelLayer layer : PixelLayer.values()) {
                PixleClient.INSTANCE.getClient().sendTCP(new RequestRegionPacket(regionX, regionY, layer.ordinal()));
            }
        }
    }

    public void completeRegion(int regionX, int regionY) {
        requestingRegions.remove(new Position(regionX, regionY));
    }
}
