package org.pixle.entity;

import net.darkhax.opennbt.tags.CompoundTag;
import org.pixle.level.Level;
import org.pixle.level.PixelLayer;
import org.pixle.network.InventoryUpdatePacket;
import org.pixle.network.PixleNetworkManager;
import org.pixle.pixel.PixelStack;
import org.pixle.server.PixleServer;

import java.util.List;
import java.util.Random;

public class PixelEntity extends Entity {
    public PixelStack pixel;

    public PixelEntity(Level level) {
        super(level);
        this.setBounds(0.5F, 0.5F);
    }

    public PixelEntity(Level level, PixelStack pixel) {
        this(level);
        this.pixel = pixel;
    }

    public PixelEntity(Level level, int x, int y, PixelLayer layer) {
        this(level, new PixelStack(level.getPixel(x, y, layer), 1));
        this.posX = x + new Random().nextFloat();
        this.posY = y;
    }

    @Override
    public void update() {
        super.update();
        if (level.getSide().isServer()) {
            List<Entity> collidingEntities = level.getCollidingEntities(bounds);
            for (Entity entity : collidingEntities) {
                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entity;
                    int slot = player.getInventory().addStackToInventory(pixel);
                    PixleServer.INSTANCE.getServer().sendToTCP(PixleNetworkManager.getConnection(player).getID(), new InventoryUpdatePacket(player, slot));
                    destroy();
                    break;
                }
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag compound) {
        super.writeToNBT(compound);
        CompoundTag pixelTag = new CompoundTag("pixel");
        pixel.writeToNBT(pixelTag);
        compound.setTag(pixelTag);
    }

    @Override
    public void readFromNBT(CompoundTag compound) {
        super.readFromNBT(compound);
        CompoundTag pixelTag = compound.getCompoundTag("pixel");
        pixel = PixelStack.readFromNBT(pixelTag);
    }

    @Override
    public String writeData() {
        return String.valueOf(pixel.writeData());
    }

    @Override
    public void readData(String string) {
        pixel = PixelStack.readData(string);
    }
}
