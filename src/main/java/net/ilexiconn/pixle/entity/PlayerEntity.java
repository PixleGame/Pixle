package net.ilexiconn.pixle.entity;

import net.darkhax.opennbt.tags.CompoundTag;
import net.ilexiconn.pixle.entity.inventory.PlayerInventory;
import net.ilexiconn.pixle.level.Level;

public class PlayerEntity extends Entity {
    private PlayerInventory inventory;
    public String username;

    public PlayerEntity(Level level) {
        super(level);
        this.inventory = new PlayerInventory();
    }

    public PlayerEntity(Level level, String username) {
        this(level);
        this.username = username;
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
}
