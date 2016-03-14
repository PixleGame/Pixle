package net.ilexiconn.pixle.entity;

import net.darkhax.opennbt.tags.CompoundTag;
import net.ilexiconn.pixle.entity.inventory.PlayerInventory;
import net.ilexiconn.pixle.level.Level;

public class PlayerEntity extends Entity {
    private PlayerInventory inventory;
    public String username;
    public boolean jumping;
    public float moveX;

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
        CompoundTag tag = new CompoundTag("inventory");
        inventory.writeToNBT(tag);
        compound.setTag(tag);
    }

    @Override
    public void readFromNBT(CompoundTag compound) {
        super.readFromNBT(compound);
        username = compound.getString("username");
        inventory.readFromNBT(compound.getCompoundTag("inventory"));
    }

    @Override
    public void writeData(ByteBuffer buffer) {
        buffer.writeStringByte(username);
    }

    @Override
    public void readData(ByteBuffer buffer) {
        username = buffer.readStringByte();
    }
}
