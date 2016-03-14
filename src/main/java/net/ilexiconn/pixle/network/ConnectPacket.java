package net.ilexiconn.pixle.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.server.PixleServer;

public class ConnectPacket {
    private String username;

    public ConnectPacket() {}

    public ConnectPacket(String username) {
        this.username = username;
    }

    public void receive(Connection connection) {
        PixleServer instance = PixleServer.INSTANCE;
        int connectionId = connection.getID();
        Server server = (Server) connection.getEndPoint();
        if (PixleNetworkManager.addClient(connection, username)) {
            System.out.println(username + " has connected to the server!");
            Level level = instance.getLevel();
            PlayerEntity player = new PlayerEntity(level, username);
            player.posY = level.getHeight((int) player.posX, PixelLayer.FOREGROUND) + 1;
            level.addEntity(player, true);
            server.sendToTCP(connectionId, new SetPlayerPacket(player));
            for (Entity entity : level.getEntities()) {
                server.sendToTCP(connectionId, new AddEntityPacket(entity));
            }
        } else {
            System.out.println(username + " tried to join but somebody with that username is connected!");
            connection.close();
        }
    }
}
