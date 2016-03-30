package org.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import org.pixle.client.PixleClient;
import org.pixle.entity.PlayerEntity;
import org.pixle.server.PixleServer;

public class SelectSlotPacket extends PixlePacket {
    private int selectedItem;

    public SelectSlotPacket() {
    }

    public SelectSlotPacket(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    @Override
    public void handleServer(PixleServer server, PlayerEntity player, Connection connection, long estimatedSendTime) {
        player.selectedItem = selectedItem;
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {

    }
}
