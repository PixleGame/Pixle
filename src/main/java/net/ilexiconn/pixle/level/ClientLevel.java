package net.ilexiconn.pixle.level;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.network.RequestRegionPacket;
import net.ilexiconn.pixle.util.Side;

import java.util.ArrayList;
import java.util.List;

public class ClientLevel extends Level {
    private List<Integer> requestingRegions = new ArrayList<>();

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void requestRegion(Region region, int regionX) {
        if (!requestingRegions.contains(regionX)) {
            requestingRegions.add(regionX);
            PixleClient.INSTANCE.getClient().sendPacketToServer(new RequestRegionPacket(regionX));
        }
    }

    public void receiveRegion(Region region) {
        Integer x = region.getX();
        requestingRegions.remove(x);
    }
}
