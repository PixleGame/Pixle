package net.ilexiconn.pixle.plugin;

import com.esotericsoftware.minlog.Log;
import net.ilexiconn.pixle.event.bus.EventHandler;
import net.ilexiconn.pixle.event.event.PixleInitializeEvent;
import net.ilexiconn.pixle.pixel.Pixel;

public class TestPlugin {
    public static final Pixel custom;

    static {
        custom = new Pixel(100).setColor(0x000000);
    }

    @EventHandler
    public void onInitialize(PixleInitializeEvent event) {
        Log.info("TestPlugin", "Hello world!");
    }
}
