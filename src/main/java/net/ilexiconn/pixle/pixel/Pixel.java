package net.ilexiconn.pixle.pixel;

import net.ilexiconn.pixle.pixel.properties.DefaultPixelProperties;
import net.ilexiconn.pixle.pixel.properties.PixelProperties;

import java.util.HashMap;
import java.util.Map;

public class Pixel {
    public static final int STONE = 0x909090;
    public static final int BEDROCK = 0x333333;
    public static final int GRASS = 0x71C476;
    public static final int DIRT = 0x7C5B44;
    public static final int AIR = 0x0094FF;

    private static final PixelProperties DEFAULT_PROPERTIES = new DefaultPixelProperties();
    private static Map<Integer, PixelProperties> properties = new HashMap<>();

    static {
        registerProperties(BEDROCK, () -> false);
    }

    public static PixelProperties getProperties(int pixel) {
        PixelProperties pixelProperties = properties.get(pixel);
        if (pixelProperties == null) {
            return DEFAULT_PROPERTIES;
        } else {
            return pixelProperties;
        }
    }

    public static void registerProperties(int pixel, PixelProperties pixelProperties) {
        properties.put(pixel, pixelProperties);
    }
}
