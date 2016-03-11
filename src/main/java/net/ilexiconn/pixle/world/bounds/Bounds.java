package net.ilexiconn.pixle.world.bounds;

public abstract class Bounds {
    public boolean intersects(Bounds bounds) {
        return (bounds.getMaxX() > getMinX() && bounds.getMinX() < getMaxX()) && (bounds.getMaxY() > getMinY() && bounds.getMinY() < getMaxY());
    }

    public boolean intersects(double x, double y) {
        return (x > getMinX() && x < getMaxX()) && (y > getMinY() && y < getMaxY());
    }

    public double preventIntersectionX(double posX, Bounds intersecting) {
        if (getMinX() < intersecting.getMaxX()) {
            posX = intersecting.getMaxX();
        } else if (getMaxX() > intersecting.getMinX()) {
            posX = intersecting.getMinX();
        }
        return posX;
    }

    public double preventIntersectionY(double posY, Bounds intersecting) {
        if (getMinY() < intersecting.getMaxY()) {
            posY = intersecting.getMaxY();
        } else if (getMaxY() > intersecting.getMinY()) {
            posY = intersecting.getMinY();
        }
        return posY;
    }

    public abstract float getMaxX();
    public abstract float getMinX();

    public abstract float getMaxY();
    public abstract float getMinY();

    public abstract float getSizeX();
    public abstract float getSizeY();
}
