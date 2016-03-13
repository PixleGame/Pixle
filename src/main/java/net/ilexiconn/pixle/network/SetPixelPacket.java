package net.ilexiconn.pixle.network;

import net.ilexiconn.netconn.ByteBuffer;
import net.ilexiconn.netconn.INetworkManager;
import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.server.PixleServer;

import java.net.Socket;

public class SetPixelPacket extends PixlePacket {
    private int pixel;
    private int x;
    private int y;
    private PixelLayer layer;

    public SetPixelPacket() {}

    public SetPixelPacket(int pixel, int x, int y, PixelLayer layer) {
        this.pixel = pixel;
        this.x = x;
        this.y = y;
        this.layer = layer;
    }

    @Override
    public void handleServer(PixleServer server, Socket sender, PlayerEntity player, INetworkManager networkManager) {
    }

    @Override
    public void handleClient(PixleClient client, INetworkManager networkManager) {
        client.getLevel().setPixel(pixel, x, y, layer);
    }

    @Override
    public void encode(ByteBuffer buffer) {
        buffer.writeInteger(pixel);
        buffer.writeInteger(x);
        buffer.writeInteger(y);
        buffer.writeByte((byte) layer.ordinal());
    }

    @Override
    public void decode(ByteBuffer buffer) {
        pixel = buffer.readInteger();
        x = buffer.readInteger();
        y = buffer.readInteger();
        layer = PixelLayer.values()[buffer.readByte()];
    }
}
