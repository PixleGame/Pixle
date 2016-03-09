package net.ilexiconn.pixle;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.server.PixleServer;

public class Main {
    public static void main(String[] args) {
        if (args.length == 1) {
            switch (args[0]) {
                case "client": {
                    PixleClient client = new PixleClient();
                    client.start();
                }
                case "server": {
                    PixleServer server = new PixleServer();
                    server.start();
                }
            }
        } else {
            PixleClient client = new PixleClient();
            client.start();
        }
    }
}
