package org.pixle.client.gui.component;

import java.lang.reflect.Field;

public abstract class GUIOptionComponent extends GUIComponent {
    public GUIOptionComponent(int x, int y) {
        super(x, y);
    }

    public abstract void set(Field field) throws IllegalAccessException;
}