package net.ilexiconn.pixle.client.event;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.event.Cancelable;

public class RenderEntityEvent extends RenderEvent {
    private Entity entity;

    public RenderEntityEvent(PixleClient client, Entity entity) {
        super(client);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    @Cancelable
    public static class Pre extends RenderEntityEvent {
        public Pre(PixleClient client, Entity entity) {
            super(client, entity);
        }
    }

    public static class Post extends RenderEntityEvent {
        public Post(PixleClient client, Entity entity) {
            super(client, entity);
        }
    }
}
