package org.pixle.level;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Position) {
            Position pos = (Position) object;
            return pos.x == x && pos.y == y;
        }
        return false;
    }
}
