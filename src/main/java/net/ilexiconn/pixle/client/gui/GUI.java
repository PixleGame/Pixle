package net.ilexiconn.pixle.client.gui;

import net.ilexiconn.pixle.client.PixleClient;

public abstract class GUI {
    protected PixleClient pixle;

    public GUI(PixleClient pixle) {
        this.pixle = pixle;
    }

    public abstract void render();
}
