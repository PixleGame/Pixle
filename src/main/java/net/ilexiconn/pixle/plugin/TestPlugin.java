package net.ilexiconn.pixle.plugin;

import com.esotericsoftware.minlog.Log;
import net.ilexiconn.pixle.event.bus.EventHandler;
import net.ilexiconn.pixle.event.GenerateRegionEvent;
import net.ilexiconn.pixle.event.PixleInitializeEvent;
import net.ilexiconn.pixle.event.SetPixelEvent;
import net.ilexiconn.pixle.util.Side;

public class TestPlugin {
    @EventHandler
    public void onInitialize(PixleInitializeEvent event) {
        Log.info("TestPlugin/" + Side.get(), "Hello world!");
    }

    @EventHandler
    public void onSetPixel(SetPixelEvent event) {
//        if (event.getPixel() == Pixel.grass) {
//            event.setCanceled();
//        }
    }

    @EventHandler
    public void onGenerateRegion(GenerateRegionEvent.Post event) {
//        for (int layer = 0; layer < PixelLayer.values().length; layer++) {
//            for (int x = 0; x < Region.REGION_WIDTH; x++) {
//                for (int y = 0; y < Region.REGION_HEIGHT; y++) {
//                    if (event.getRegion().getPixels()[layer][x][y] == Pixel.grass.getPixelID()) {
//                        event.getRegion().setPixel(Pixel.air, x, y, PixelLayer.values()[layer]);
//                    }
//                }
//            }
//        }
    }
}
