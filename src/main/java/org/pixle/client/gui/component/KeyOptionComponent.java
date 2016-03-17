package org.pixle.client.gui.component;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.TrueTypeFont;
import org.pixle.client.PixleClient;
import org.pixle.client.gl.GLStateManager;
import org.pixle.client.render.RenderHelper;

import java.lang.reflect.Field;

public class KeyOptionComponent extends GUIOptionComponent {
    protected int width;
    protected int height;
    protected IComponentTheme theme;
    protected boolean selected;
    protected int key;

    public static final IComponentTheme DEFAULT_THEME = new IComponentTheme() {
        @Override
        public int getPrimaryColor(boolean selected) {
            return selected ? 0x666666 : 0x999999;
        }

        @Override
        public int getSecondaryColor(boolean selected) {
            return selected ? 0x404040 : 0x666666;
        }
    };

    public KeyOptionComponent(int x, int y, int width, int height, int key, IComponentTheme theme) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.key = key;
        this.theme = theme;
    }

    public KeyOptionComponent(int x, int y, int width, int height, int key) {
        this(x, y, width, height, key, DEFAULT_THEME);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        boolean selected = isMouseOver(mouseX, mouseY) || this.selected;

        GLStateManager.setColor(theme.getPrimaryColor(selected));
        RenderHelper.drawRect(x, y, width, height);
        GLStateManager.setColor(theme.getSecondaryColor(selected));
        RenderHelper.drawOutline(x, y, width, height, 2);

        GLStateManager.enableTexture();
        TrueTypeFont font = PixleClient.INSTANCE.getFontRenderer();
        String text = Keyboard.getKeyName(key);
        RenderHelper.drawCenteredScaledString(x + (width / 2), y + (height / 2) + 6, text, 1.0F);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            selected = true;
        }
    }

    @Override
    public void keyPressed(char c, int key) {
        if (selected) {
            this.key = key;
            selected = false;
        }
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public int getKey() {
        return key;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void set(Field field) throws IllegalAccessException {
        field.set(PixleClient.INSTANCE.config, key);
    }
}
