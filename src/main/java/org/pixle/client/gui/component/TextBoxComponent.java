package org.pixle.client.gui.component;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.TrueTypeFont;
import org.pixle.client.PixleClient;
import org.pixle.client.gl.GLStateManager;
import org.pixle.client.render.RenderHelper;

public class TextBoxComponent extends GUIComponent {
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
    private String text;
    private int width;
    private int height;
    private IComponentTheme theme;
    private boolean selected;

    public TextBoxComponent(int x, int y, int width, int height, String startText, IComponentTheme theme) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.text = startText;
        this.theme = theme;
    }

    public TextBoxComponent(int x, int y, int width, int height, String startText) {
        this(x, y, width, height, startText, DEFAULT_THEME);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        GLStateManager.setColor(theme.getPrimaryColor(selected));
        RenderHelper.drawRect(x, y, width, height);
        GLStateManager.setColor(theme.getSecondaryColor(selected));
        RenderHelper.drawOutline(x, y, width, height, 2);

        TrueTypeFont font = PixleClient.INSTANCE.getFontRenderer();
        String drawText = text;
        if (selected && System.currentTimeMillis() % 1000 > 500) {
            drawText += "|";
        }
        GLStateManager.setColor(0xFFFFFF);
        RenderHelper.drawScaledStringWithShadow((x + (width / 2)) - (font.getWidth(text)), (y + (height / 2)) - (font.getHeight(text)) + 12, drawText, 2.0F);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        selected = isMouseOver(mouseX, mouseY);
    }

    @Override
    public void keyPressed(char c, int key) {
        if (selected) {
            if (key == Keyboard.KEY_BACK) {
                if (text.length() > 0) {
                    text = text.substring(0, text.length() - 1);
                }
            } else if (c != 167 && c >= 32 && c != 127) {
                text += c;
            }
        }
    }

    @Override
    public void mouseDown(int mouseX, int mouseY) {
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public String getText() {
        return text;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
