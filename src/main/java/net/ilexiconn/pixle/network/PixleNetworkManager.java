package net.ilexiconn.pixle.network;

import net.ilexiconn.netconn.NetconnRegistry;
import net.ilexiconn.netconn.server.IServerListener;
import net.ilexiconn.netconn.server.Server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PixleNetworkManager {
    public static Map<Socket, String> clients = new HashMap<>();

    public static void init() {
        NetconnRegistry.registerPacket(0, ConnectPacket.class);
        NetconnRegistry.registerPacket(1, RequestRegionPacket.class);
        NetconnRegistry.registerPacket(2, SendRegionPacket.class);
        NetconnRegistry.registerPacket(3, AddEntityPacket.class);
        NetconnRegistry.registerPacket(4, RemoveEntityPacket.class);
        NetconnRegistry.registerPacket(5, EntityPositionUpdatePacket.class);
        NetconnRegistry.registerPacket(6, PlayerMovePacket.class);
    }

    public static boolean addClient(Socket socket, String username) {
        if (!clients.containsValue(username)) {
            clients.put(socket, username);
            return true;
        }
        return false;
    }

    public static String getUsername(Socket sender) {
        return clients.get(sender);
    }
}
