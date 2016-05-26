package org.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import org.pixle.client.PixleClient;
import org.pixle.client.gui.GUI;
import org.pixle.client.gui.LevelGUI;
import org.pixle.client.message.MessageBubble;
import org.pixle.entity.Entity;
import org.pixle.entity.PlayerEntity;
import org.pixle.server.PixleServer;

public class SendMessagePacket extends PixlePacket {
    private int entityID;
    private String message;

    public SendMessagePacket() {
    }

    public SendMessagePacket(PlayerEntity entity, String message) {
        this.entityID = entity.entityID;
        this.message = message;
    }

    @Override
    public void handleServer(PixleServer server, PlayerEntity player, Connection connection, long estimatedSendTime) {
        server.getServer().sendToAllTCP(this);
    }

    @Override
    public void handleClient(PixleClient client, Connection connection, long estimatedSendTime) {
        LevelGUI levelGUI = null;
        for (GUI gui : client.getOpenGUIs()) {
            if (gui instanceof LevelGUI) {
                levelGUI = (LevelGUI) gui;
            }
        }
        Entity entity = client.getLevel().getEntityById(entityID);
        levelGUI.bubbleList.add(new MessageBubble(message, (int) entity.posX, (int) entity.posY));
    }
}
