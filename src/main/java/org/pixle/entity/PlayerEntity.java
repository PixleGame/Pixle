package org.pixle.entity;

import net.darkhax.opennbt.tags.CompoundTag;
import org.pixle.client.PixleClient;
import org.pixle.entity.inventory.PlayerInventory;
import org.pixle.level.Level;
import org.pixle.level.PixelLayer;
import org.pixle.network.SetPixelPacket;
import org.pixle.pixel.Pixel;

public class PlayerEntity extends Entity {
    private PlayerInventory inventory;
    public String username;
    public boolean jumping;
    public float moveX;
    public int selectedItem;

    public static final int REACH_DISTANCE = 15;

    public PlayerEntity(Level level) {
        super(level);
        this.inventory = new PlayerInventory();
    }

    public PlayerEntity(Level level, String username) {
        this(level);
        this.username = username;
    }

    @Override
    public void update() {
        super.update();
        if (jumping && onSurface) {
            velY = 1.0F;
        }
        velX = moveX;
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    @Override
    public void writeToNBT(CompoundTag compound) {
        super.writeToNBT(compound);
        compound.setString("username", username);
        compound.setInt("selectedItem", selectedItem);
        CompoundTag inventoryTag = new CompoundTag("inventory");
        inventory.writeToNBT(inventoryTag);
        compound.setTag(inventoryTag);
    }

    @Override
    public void readFromNBT(CompoundTag compound) {
        super.readFromNBT(compound);
        username = compound.getString("username");
        selectedItem = compound.getInt("selectedItem");
        inventory.readFromNBT(compound.getCompoundTag("inventory"));
    }

    @Override
    public String writeData() {
        return username;
    }

    @Override
    public void readData(String string) {
        username = string;
    }

    public void setPixel(Pixel pixel, int x, int y, PixelLayer layer) {
        level.setPixel(pixel, x, y, layer);
        PixleClient.INSTANCE.getClient().sendTCP(new SetPixelPacket(pixel.getPixelID(), x, y, layer));
    }
}
