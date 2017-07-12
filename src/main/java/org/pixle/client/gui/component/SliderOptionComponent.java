package org.pixle.client.gui.component;

import org.pixle.client.gl.GLStateManager;
import org.pixle.client.render.RenderHelper;

public class SliderOptionComponent extends GUIOptionComponent {
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
    protected int minValue;
    protected int maxValue;
    protected int width;
    protected int value;
    protected IComponentTheme theme;

    public SliderOptionComponent(int x, int y, int minValue, int maxValue, int width, int startingValue, IComponentTheme theme) {
        super(x, y);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.width = width;
        this.value = startingValue;
        this.theme = theme;
    }

    public SliderOptionComponent(int x, int y, int minValue, int maxValue, int width, int startingValue) {
        this(x, y, minValue, maxValue, width, startingValue, DEFAULT_THEME);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        GLStateManager.setColor(theme.getSecondaryColor(false));
        RenderHelper.drawRect(x, y + 15, width, 2);
        int scaledValue = getScaledValue();
        boolean selected = mouseOver(mouseX, mouseY);
        GLStateManager.setColor(theme.getPrimaryColor(selected));
        RenderHelper.drawRect(x + scaledValue, y, 10, 30);
        GLStateManager.setColor(theme.getSecondaryColor(selected));
        RenderHelper.drawOutline(x + scaledValue, y, 10, 30, 2);
    }

    private int getScaledValue() {
        return (int) (((double) value / (double) (maxValue - minValue) * (double) width) + minValue);
    }

    private int getValue(int scaledValue) {
        return (int) (((double) (scaledValue - minValue) / (double) width) * (double) (maxValue - minValue));
    }

    private boolean mouseOver(int mouseX, int mouseY) {
        int scaledValue = getScaledValue();
        return mouseX > x + scaledValue && mouseX < x + scaledValue + 10 && mouseY > y && mouseY < y + 30;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
    }

    @Override
    public void keyPressed(char c, int key) {

    }

    @Override
    public void mouseDown(int mouseX, int mouseY) {
        if (mouseY > y && mouseY < y + 30) {
            int scaledValue = Math.min(Math.max(0, (mouseX - x) - 5), width);
            value = getValue(scaledValue);
        }
    }

    @Override
    public Object set() {
        return value;
    }
}
