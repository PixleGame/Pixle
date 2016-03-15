package net.ilexiconn.pixle.level;

import net.ilexiconn.pixle.level.region.ReceivingRegion;
import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.util.Side;

import java.util.ArrayList;
import java.util.List;

public class ClientLevel extends Level {
    private List<ReceivingRegion> requestingRegions = new ArrayList<>();

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void requestRegion(Region region, int regionX) {
        ReceivingRegion receivingRegion = new ReceivingRegion(regionX);
        if (!requestingRegions.contains(receivingRegion)) {
            requestingRegions.add(receivingRegion);
            receivingRegion.requestNext();
        }
    }

    public ReceivingRegion getReceivingRegion(int x) {
        for (ReceivingRegion receivingRegion : new ArrayList<>(requestingRegions)) {
            if (receivingRegion.getX() == x) {
                return receivingRegion;
            }
        }
        return null;
    }

    public void completeRegion(ReceivingRegion receivingRegion) {
        requestingRegions.remove(receivingRegion);
    }
}
