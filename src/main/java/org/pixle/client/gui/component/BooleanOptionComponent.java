package org.pixle.client.gui.component;

import org.pixle.client.gl.GLStateManager;
import org.pixle.client.render.RenderHelper;

public class BooleanOptionComponent extends GUIOptionComponent {
    public static final IComponentTheme DEFAULT_THEME = new IComponentTheme() {
        @Override
        public int getPrimaryColor(boolean selected) {
            return selected ? 0x888888 : 0x999999;
        }

        @Override
        public int getSecondaryColor(boolean selected) {
            return selected ? 0x555555 : 0x666666;
        }
    };
    protected int width;
    protected int height;
    protected boolean value;
    protected IComponentTheme theme;

    public BooleanOptionComponent(int x, int y, int width, int height, boolean startingValue, IComponentTheme theme) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.value = startingValue;
        this.theme = theme;
    }

    public BooleanOptionComponent(int x, int y, int width, int height, boolean startingValue) {
        this(x, y, width, height, startingValue, DEFAULT_THEME);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        boolean selected = isMouseOver(mouseX, mouseY);
        GLStateManager.setColor(theme.getPrimaryColor(selected));
        RenderHelper.drawRect(x, y, width, height);
        GLStateManager.setColor(theme.getSecondaryColor(selected));
        RenderHelper.drawOutline(x, y, width, height, 2);

        String text = value ? "ON" : "OFF";
        RenderHelper.drawCenteredScaledString(x + (width / 2), y + (height / 2) + 6, text, 2.0F);
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            value = !value;
        }
    }

    @Override
    public void keyPressed(char c, int key) {

    }

    @Override
    public void mouseDown(int mouseX, int mouseY) {

    }

    @Override
    public Object set() {
        return value;
    }
}
