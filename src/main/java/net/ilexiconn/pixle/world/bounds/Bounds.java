package net.ilexiconn.pixle.world.bounds;

public abstract class Bounds {
    public boolean intersects(Bounds bounds) {
        return (getMinY() < bounds.getMaxY() || getMaxY() > bounds.getMinY()) && (getMinX() < bounds.getMaxX() || getMaxX() > bounds.getMinX());
    }

    public boolean intersects(double x, double y) {
        return (x > getMinX() && x < getMaxX()) && (y > getMinY() && y < getMaxY());
    }

    public double preventIntersectionX(double posX, Bounds intersecting) {
        if (getMinY() < intersecting.getMaxY() && getMaxY() > intersecting.getMinY()) {
            if (getMinX() < intersecting.getMaxX() && getMinX() > intersecting.getMinX()) {
                posX = intersecting.getMaxX();
            } else if (getMaxX() > intersecting.getMinX() && getMaxX() < intersecting.getMaxX()) {
                posX = intersecting.getMinX();
            }
        }
        return posX;
    }

    public double preventIntersectionY(double posY, Bounds intersecting) {
        if (getMinX() < intersecting.getMaxX() && getMaxX() > intersecting.getMinX()) {
            if (getMinY() < intersecting.getMaxY() && getMinY() > intersecting.getMinY()) {
                posY = intersecting.getMaxY();
            } else if (getMaxY() > intersecting.getMinY() && getMaxY() < intersecting.getMaxY()) {
                posY = intersecting.getMinY();
            }
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
