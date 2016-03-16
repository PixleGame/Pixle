package org.pixle.pixel;

public class Pixel {
    private static Pixel[] pixels = new Pixel[256];

    public static final Pixel air;
    public static final Pixel grass;
    public static final Pixel stone;
    public static final Pixel dirt;
    public static final Pixel log;
    public static final Pixel leaves;
    public static final Pixel bedrock;

    static {
        air = new Pixel(0);
        grass = new Pixel(1).setColor(0x71C476);
        stone = new Pixel(2).setColor(0x909090);
        dirt = new Pixel(3).setColor(0x7C5B44);
        log = new Pixel(4).setColor(0xB97A57);
        leaves = new Pixel(5).setColor(0x22B14C);
        bedrock = new Pixel(6).setColor(0x333333);
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
        return Pixel.air;
    }
}
