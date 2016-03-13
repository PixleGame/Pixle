package net.ilexiconn.pixle.network;

import net.ilexiconn.netconn.ByteBuffer;
import net.ilexiconn.netconn.INetworkManager;
import net.ilexiconn.netconn.IPacket;

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
        PixleNetworkManager.addClient(sender, username);
        System.out.println(username + " has connected to the server!");
    }

    @Override
    public void handleClient(Socket server, INetworkManager networkManager) {
    }
}
