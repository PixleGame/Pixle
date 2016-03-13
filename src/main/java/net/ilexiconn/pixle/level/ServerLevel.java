package net.ilexiconn.pixle.level;

import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.util.Side;

public class ServerLevel extends Level {
    @Override
    public Side getSide() {
        return Side.SERVER;
    }

    @Override
    public void requestRegion(Region region, int regionX) {
        region.generate(seed);
    }
}
