package org.pixle.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import org.pixle.entity.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class PixleNetworkManager {
    public static Map<Connection, String> clients = new HashMap<>();

    public static void init(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(byte[].class);
        kryo.register(byte[][].class);
        kryo.register(HashMap.class);
        kryo.register(ConnectPacket.class);
        kryo.register(RequestRegionPacket.class);
        kryo.register(ConnectPacket.class);
        kryo.register(RequestRegionPacket.class);
        kryo.register(SendRegionPacket.class);
        kryo.register(AddEntityPacket.class);
        kryo.register(RemoveEntityPacket.class);
        kryo.register(EntityPositionUpdatePacket.class);
        kryo.register(PlayerMovePacket.class);
        kryo.register(SetPlayerPacket.class);
        kryo.register(SetPixelPacket.class);
        kryo.register(SendMessagePacket.class);
        kryo.register(InventoryUpdatePacket.class);
        kryo.register(SelectSlotPacket.class);
    }

    public static boolean addClient(Connection connection, String username) {
        if (!clients.containsValue(username)) {
            clients.put(connection, username);
            return true;
        }
        return false;
    }

    public static String getUsername(Connection connection) {
        return clients.get(connection);
    }

    public static Connection getConnection(PlayerEntity player) {
        for (Map.Entry<Connection, String> entry : clients.entrySet()) {
            if (entry.getValue().equals(player.username)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
