package net.ilexiconn.pixle.client.gui.component;

public abstract class GUIComponent {
    protected int x;
    protected int y;

    public GUIComponent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void render(int mouseX, int mouseY);

    public abstract void mouseClicked(int mouseX, int mouseY);

    public abstract void keyPressed(char c, int key);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
