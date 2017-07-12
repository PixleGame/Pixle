package org.pixle.pixel;

import net.darkhax.opennbt.tags.CompoundTag;

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

    public static PixelStack readFromNBT(CompoundTag tag) {
        return new PixelStack(Pixel.getPixelByID(tag.getByte("pixel")), tag.getInt("size"));
    }

    public static PixelStack readData(String string) {
        String[] data = string.split(",");
        return new PixelStack(Pixel.getPixelByID(Integer.parseInt(data[0])), Integer.parseInt(data[1]));
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

    public void writeToNBT(CompoundTag tag) {
        tag.setInt("size", size);
        tag.setByte("pixel", (byte) pixel.getPixelID());
    }

    public String writeData() {
        return pixel.getPixelID() + "," + size;
    }
}
