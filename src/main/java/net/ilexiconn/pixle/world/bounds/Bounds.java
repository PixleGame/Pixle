package net.ilexiconn.pixle.world.bounds;

public abstract class Bounds {
    public boolean intersects(Bounds bounds) {
        return (bounds.getMaxX() > getMinX() && bounds.getMinX() < getMaxX()) && (bounds.getMaxY() > getMinY() && bounds.getMinY() < getMaxY());
    }

    public boolean intersects(double x, double y) {
        return (x > getMinX() && x < getMaxX()) && (y > getMinY() && y < getMaxY());
    }

    public double getXIntersectOffset(Bounds bounds) {
        if (bounds.getMaxY() > getMinY() && bounds.getMinY() < getMaxY()) {
            return getMinX() - bounds.getMinX();
        }
        return 0.0F;
    }

    public double getYIntersectOffset(Bounds bounds) {
        if (bounds.getMaxX() > getMinX() && bounds.getMinX() < getMaxX()) {
            return bounds.getMaxY() - getMinY();
        }
        return 0.0F;
    }

    public abstract float getMaxX();
    public abstract float getMinX();

    public abstract float getMaxY();
    public abstract float getMinY();

    public abstract float getSizeX();
    public abstract float getSizeY();
}
