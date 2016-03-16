package org.pixle.entity.inventory;

import net.darkhax.opennbt.tags.CompoundTag;
import net.darkhax.opennbt.tags.Tag;
import org.pixle.pixel.PixelStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Inventory {
    private PixelStack[] inventory = new PixelStack[getSlotCount()];

    public PixelStack getPixelStack(int index) {
        return inventory[index];
    }

    public void setPixelStack(PixelStack pixel, int index) {
        inventory[index] = pixel;
    }

    public void addStackToInventory(PixelStack stack) {
        int emptySlot = -1;
        for (int i = 0; i < getSlotCount(); i++) {
            PixelStack stackInSlot = getPixelStack(i);
            if (stackInSlot != null) {
                if (stackInSlot.getPixel() == stack.getPixel()) {
                    stackInSlot.merge(stack);
                    return;
                }
            } else {
                if (emptySlot == -1) {
                    emptySlot = i;
                }
            }
        }
        if (emptySlot != -1) {
            setPixelStack(stack, emptySlot);
        }
    }

    public void writeToNBT(CompoundTag tag) {
        List<Tag> pixelList = new ArrayList<>();
        for (int i = 0; i < getSlotCount(); i++) {
            PixelStack stack = getPixelStack(i);
            if (stack != null) {
                CompoundTag pixelTag = new CompoundTag(i + "");
                pixelTag.setInt("color", stack.getPixel());
                pixelTag.setInt("size", stack.getSize());
                pixelList.add(pixelTag);
            }
        }
        tag.setTagList("pixels", pixelList);
    }

    public void readFromNBT(CompoundTag tag) {
        List<Tag> pixelList = tag.getTagList("pixels");
        for (Tag pixelTag : pixelList) {
            CompoundTag pixelCompoundTag = (CompoundTag) pixelTag;
            setPixelStack(new PixelStack(pixelCompoundTag.getInt("color"), pixelCompoundTag.getInt("size")), Integer.parseInt(pixelCompoundTag.getName()));
        }
    }

    public abstract int getSlotCount();
}
