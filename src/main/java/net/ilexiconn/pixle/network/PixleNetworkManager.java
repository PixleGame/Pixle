package net.ilexiconn.pixle.network;

import net.ilexiconn.netconn.NetconnRegistry;
import net.ilexiconn.netconn.server.IServerListener;
import net.ilexiconn.netconn.server.Server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PixleNetworkManager implements IServerListener {
    private static Map<Socket, String> clients = new HashMap<>();

    public static void init() {
        NetconnRegistry.registerPacket(0, ConnectPacket.class);
        NetconnRegistry.registerPacket(1, RequestRegionPacket.class);
        NetconnRegistry.registerPacket(2, SendRegionPacket.class);
    }

    public static void addClient(Socket socket, String username) {
        clients.put(socket, username);
    }

    public static String getUsername(Socket sender) {
        return clients.get(sender);
    }

    @Override
    public void onClientConnected(Server server, Socket client) {
    }

    @Override
    public void onClientDisconnected(Server server, Socket client) {
        System.out.println(clients.get(client) + " has disconnected!");
        clients.remove(client);
    }
}
