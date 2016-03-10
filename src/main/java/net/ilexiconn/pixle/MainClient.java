package net.ilexiconn.pixle;

import net.ilexiconn.pixle.client.PixleClient;

public class MainClient {
    public static void main(String[] args) {
        PixleClient client = new PixleClient();
        client.start();
    }
}
