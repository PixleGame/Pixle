package net.ilexiconn.pixle.network;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.level.ClientLevel;
import net.ilexiconn.pixle.level.PixelLayer;
import net.ilexiconn.pixle.level.region.Region;
import net.ilexiconn.pixle.server.PixleServer;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SendRegionPacket extends PixlePacket {
    private int[][] pixels;
    private int regionX;
    private PixelLayer layer;

    public SendRegionPacket() {}

    public SendRegionPacket(Region region, PixelLayer layer) {
        this.pixels = region.getPixels()[layer.ordinal()];
        this.regionX = region.getX();
        this.layer = layer;
    }

    @Override
    public void handleServer(PixleServer server, Socket sender, PlayerEntity player, INetworkManager networkManager, long estimatedSendTime) {
    }

    @Override
    public void handleClient(PixleClient client, INetworkManager networkManager, long estimatedSendTime) {
        ClientLevel level = client.getLevel();
        Region region = level.getRegion(regionX);
        region.setPixels(pixels, layer);
        level.receiveRegion(region);
    }

    @Override
    public void encode(ByteBuffer buffer) {
        buffer.writeByte((byte) layer.ordinal());
        buffer.writeInteger(regionX);

        int id = Byte.MIN_VALUE;
        Map<Integer, Integer> ids = new HashMap<>();

        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[x].length; y++) {
                int color = pixels[x][y];
                if (!ids.containsKey(color)) {
                    ids.put(color, id++);
                }
            }
        }

        buffer.writeByte((byte) (ids.size() + Byte.MIN_VALUE));

        for (Map.Entry<Integer, Integer> entry : ids.entrySet()) {
            buffer.writeByte((byte) (entry.getValue() + Byte.MIN_VALUE));
            buffer.writeInteger(entry.getKey());
        }

        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[x].length; y++) {
                buffer.writeByte((byte) (ids.get(pixels[x][y]) + Byte.MIN_VALUE));
            }
        }
    }

    @Override
    public void decode(ByteBuffer buffer) {
        layer = PixelLayer.values()[buffer.readByte()];
        regionX = buffer.readInteger();

        int width = Region.REGION_WIDTH;
        int height = Region.REGION_HEIGHT;

        pixels = new int[width][height];

        int idCount = buffer.readByte() - Byte.MIN_VALUE;

        Map<Byte, Integer> ids = new HashMap<>();

        for (int i = 0; i < idCount; i++) {
            ids.put(buffer.readByte(), buffer.readInteger());
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels[x][y] = ids.get(buffer.readByte());
            }
        }
    }
}
