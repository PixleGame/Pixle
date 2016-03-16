package org.pixle.client.event;

import org.pixle.client.gui.GUI;
import org.pixle.event.Cancelable;
import org.pixle.event.Event;

public class GUIInitializationEvent extends Event {
    private GUI gui;

    public GUIInitializationEvent(GUI gui) {
        this.gui = gui;
    }

    @Cancelable
    public static class Pre extends GUIInitializationEvent {
        public Pre(GUI gui) {
            super(gui);
        }
    }

    public static class Post extends GUIInitializationEvent {
        public Post(GUI gui) {
            super(gui);
        }
    }
}
