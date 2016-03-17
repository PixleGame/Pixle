package org.pixle.client.gui.component;

public abstract class GUIOptionComponent extends GUIComponent {
    public GUIOptionComponent(int x, int y) {
        super(x, y);
    }

    public abstract Object set();
}