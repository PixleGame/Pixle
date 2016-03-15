package net.ilexiconn.pixle.client.event;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.event.Cancelable;
import net.ilexiconn.pixle.event.Event;

public class RenderEvent extends Event {
    private PixleClient client;

    public RenderEvent(PixleClient client) {
        this.client = client;
    }

    public PixleClient getClient() {
        return client;
    }

    @Cancelable
    public static class Pre extends RenderEvent {
        public Pre(PixleClient client) {
            super(client);
        }
    }

    public static class Post extends RenderEvent {
        public Post(PixleClient client) {
            super(client);
        }
    }
}
