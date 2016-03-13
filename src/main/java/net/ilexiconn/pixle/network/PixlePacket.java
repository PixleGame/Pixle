package net.ilexiconn.pixle.network;

import net.ilexiconn.netconn.INetworkManager;
import net.ilexiconn.netconn.IPacket;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.server.PixleServer;

import java.net.Socket;

public abstract class PixlePacket implements IPacket {
    public void handleServer(Socket sender, INetworkManager networkManager) {
        PixleServer instance = PixleServer.INSTANCE;
        handleServer(instance, sender, instance.getLevel().getPlayerByUsername(PixleNetworkManager.getUsername(sender)), networkManager);
    }

    public void handleClient(Socket server, INetworkManager networkManager) {
        handleClient(PixleClient.INSTANCE, networkManager);
    }

    public abstract void handleServer(PixleServer server, Socket sender, PlayerEntity player, INetworkManager networkManager);

    public abstract void handleClient(PixleClient client, INetworkManager networkManager);
}
