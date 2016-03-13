package net.ilexiconn.pixle.network;

import net.ilexiconn.netconn.ByteBuffer;
import net.ilexiconn.netconn.INetworkManager;
import net.ilexiconn.netconn.IPacket;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.server.PixleServer;

import java.net.Socket;

public class ConnectPacket implements IPacket {
    private String username;

    public ConnectPacket() {}

    public ConnectPacket(String username) {
        this.username = username;
    }

    @Override
    public void encode(ByteBuffer buffer) {
        buffer.writeStringByte(username);
    }

    @Override
    public void decode(ByteBuffer buffer) {
        username = buffer.readStringByte();
    }

    @Override
    public void handleServer(Socket sender, INetworkManager networkManager) {
        if (PixleNetworkManager.addClient(sender, username)) {
            System.out.println(username + " has connected to the server!");
            Level level = PixleServer.INSTANCE.getLevel();
            PlayerEntity player = new PlayerEntity(level, username);
            player.posY = level.getHeight((int) player.posX, PixelLayer.FOREGROUND) + 1;
            level.addEntity(player);
        } else {
            System.out.println(username + " tried to join but somebody with that username is connected!");
            PixleServer.INSTANCE.getServer().disconnectClient(sender);
        }
    }

    @Override
    public void handleClient(Socket server, INetworkManager networkManager) {
    }
}
