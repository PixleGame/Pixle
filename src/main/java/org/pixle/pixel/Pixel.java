package org.pixle.pixel;

public class Pixel {
    private static Pixel[] pixels = new Pixel[256];

    public static final Pixel AIR;
    public static final Pixel GRASS;
    public static final Pixel STONE;
    public static final Pixel DIRT;
    public static final Pixel LOG;
    public static final Pixel LEAVES;
    public static final Pixel BEDROCK;
    public static final Pixel GRAVEL;
    public static final Pixel SNOW;

    static {
        AIR = new Pixel(0);
        GRASS = new Pixel(1).setColor(0x00A513);
        STONE = new Pixel(2).setColor(0x848484);
        DIRT = new Pixel(3).setColor(0x6D503C);
        LOG = new Pixel(4).setColor(0x4C3C23);
        LEAVES = new Pixel(5).setColor(0x2D8E00);
        BEDROCK = new Pixel(6).setColor(0x333333);
        GRAVEL = new Pixel(7).setColor(0x636363);
        SNOW = new Pixel(8).setColor(0xE8FFFF);
    }

    private final int pixelID;
    private int color;

    public Pixel(int id) {
        if (pixels[id] != null) {
            throw new RuntimeException("Pixel ID " + id + " already occupied by pixel " + Pixel.pixels[id] + " when adding pixel " + this);
        }
        pixels[pixelID = id] = this;
    }

    public int getPixelID() {
        return pixelID;
    }

    public static Pixel getPixelByID(int id) {
        return pixels[id];
    }

    public int getColor() {
        return color;
    }

    public Pixel setColor(int color) {
        this.color = color;
        return this;
    }

    public static Pixel fromColor(int color) {
        for (Pixel pixel : pixels) {
            if (pixel != null && pixel.color == color) {
                return pixel;
            }
        }
        return Pixel.AIR;
    }
}
