package org.pixle.client.event;

import org.pixle.event.Cancelable;
import org.pixle.event.Event;

public class RenderEvent extends Event {
    public RenderEvent() {
    }

    @Cancelable
    public static class Pre extends RenderEvent {
        public Pre() {

        }
    }

    public static class Post extends RenderEvent {
        public Post() {

        }
    }
}
