package net.ilexiconn.pixle.world.bounds;

public class PixelBounds extends Bounds {
    private int x;
    private int y;

    public PixelBounds(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public float getMaxX() {
        return x + getSizeX();
    }

    @Override
    public float getMinX() {
        return x;
    }

    @Override
    public float getMaxY() {
        return y + getSizeY();
    }

    @Override
    public float getMinY() {
        return y;
    }

    @Override
    public float getSizeX() {
        return 1;
    }

    @Override
    public float getSizeY() {
        return 1;
    }
}
