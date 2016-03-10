package net.ilexiconn.pixle;

import net.ilexiconn.pixle.server.PixleServer;

public class MainServer {
    public static void main(String[] args) {
        if (args.length >= 1) {
            try {
                int port = Integer.parseInt(args[0]);
                PixleServer server = new PixleServer();
                server.start(port);
            } catch (Exception e) {
                System.err.println("Non-numerical characters were in the port!s");
            }
        } else {
            System.err.println("Please specify a port to host this server on in the Program Arguments!");
        }
    }
}
