package net.ilexiconn.pixle.network;

import net.ilexiconn.netconn.INetworkManager;
import net.ilexiconn.netconn.IPacket;
import net.ilexiconn.netconn.client.Client;
import net.ilexiconn.netconn.server.Server;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.server.PixleServer;

import java.net.Socket;

public abstract class PixlePacket implements IPacket {
    public void handleServer(Socket sender, INetworkManager networkManager) {
        PixleServer instance = PixleServer.INSTANCE;
        handleServer(instance, sender, instance.getLevel().getPlayerByUsername(PixleNetworkManager.getUsername(sender)), networkManager, getEstimatedSendTime(((Server) networkManager).getPingTime(sender)));
    }

    public void handleClient(Socket server, INetworkManager networkManager) {
        handleClient(PixleClient.INSTANCE, networkManager, getEstimatedSendTime(((Client) networkManager).getPingTime()));
    }

    private long getEstimatedSendTime(int ping) {
        return System.currentTimeMillis() - (ping / 2);
    }

    public abstract void handleServer(PixleServer server, Socket sender, PlayerEntity player, INetworkManager networkManager, long estimatedSendTime);

    public abstract void handleClient(PixleClient client, INetworkManager networkManager, long estimatedSendTime);
}
