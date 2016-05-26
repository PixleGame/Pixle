package org.pixle.client.event;

import org.pixle.entity.Entity;
import org.pixle.event.Cancelable;

public class RenderEntityEvent extends RenderEvent {
    private Entity entity;

    public RenderEntityEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    @Cancelable
    public static class Pre extends RenderEntityEvent {
        public Pre(Entity entity) {
            super(entity);
        }
    }

    public static class Post extends RenderEntityEvent {
        public Post(Entity entity) {
            super(entity);
        }
    }
}
