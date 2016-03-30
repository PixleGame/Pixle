package org.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import org.pixle.client.PixleClient;
import org.pixle.entity.PlayerEntity;
import org.pixle.pixel.Pixel;
import org.pixle.pixel.PixelStack;
import org.pixle.server.PixleServer;

public class InventoryUpdatePacket extends PixlePacket {
    private int slotIndex;
    private int size;
    private byte id;

    public InventoryUpdatePacket() {
    }

    public InventoryUpdatePacket(PlayerEntity entity, int slotIndex) {
        this.slotIndex = slotIndex;
        PixelStack stack = entity.getInventory().getPixelStack(slotIndex);
        if (stack != null) {
            this.id = (byte) stack.getPixel().getPixelID();
            this.size = stack.getSize();
        }
    }

    @Override
    public void handleServer(PixleServer server, PlayerEntity player, Connection connection, long estimatedSendTime) {
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
        PlayerEntity player = client.getPlayer();
        PixelStack stack = null;
        if (id != 0 && size != 0) {
            stack = new PixelStack(Pixel.getPixelByID(id), size);
        }
        player.getInventory().setPixelStack(stack, slotIndex);
    }
}
