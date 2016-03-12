package net.ilexiconn.pixle.pixel;

public class Pixel {
    private static Pixel[] pixels = new Pixel[256];

    public static final Pixel air;
    public static final Pixel grass;
    public static final Pixel stone;
    public static final Pixel dirt;

    static {
        air = new Pixel(0);
        grass = new Pixel(1).setColor(0x71C476);
        stone = new Pixel(2).setColor(0x909090);
        dirt = new Pixel(3).setColor(0x7C5B44);
    }

    private final int pixelID;
    private int color;

    protected Pixel(int id) {
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
}
