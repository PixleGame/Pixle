package net.ilexiconn.pixle.pixel;

public class PixelStack {
    private int size;
    private Pixel pixel;

    public PixelStack(Pixel pixel, int size) {
        this.pixel = pixel;
        this.size = size;
    }

    public PixelStack(Pixel pixel) {
        this(pixel, 1);
    }

    public int getSize() {
        return size;
    }

    public Pixel getPixel() {
        return pixel;
    }

    public void increaseSize(int amount) {
        this.size += amount;
    }

    public PixelStack merge(PixelStack stack) {
        return new PixelStack(pixel, size + stack.size);
    }
}
