package org.pixle.event;

import org.pixle.level.Level;
import org.pixle.level.PixelLayer;
import org.pixle.level.region.Region;
import org.pixle.pixel.Pixel;

public class SetPixelEvent extends Event {
    private Pixel pixel;
    private int x;
    private int y;
    private Level level;
    private Region region;
    private PixelLayer layer;

    public SetPixelEvent(Pixel pixel, int x, int y, Level level, Region region, PixelLayer layer) {
        this.pixel = pixel;
        this.x = x;
        this.y = y;
        this.level = level;
        this.region = region;
        this.layer = layer;
    }

    public Pixel getPixel() {
        return pixel;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Level getLevel() {
        return level;
    }

    public Region getRegion() {
        return region;
    }

    public PixelLayer getLayer() {
        return layer;
    }

    @Cancelable
    public static class Pre extends SetPixelEvent {
        public Pre(Pixel pixel, int x, int y, Level level, Region region, PixelLayer layer) {
            super(pixel, x, y, level, region, layer);
        }
    }
}
