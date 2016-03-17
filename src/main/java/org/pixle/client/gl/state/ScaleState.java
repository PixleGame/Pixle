package org.pixle.client.gl.state;

import org.lwjgl.opengl.GL11;

public class ScaleState {
    private Double x;
    private Double y;

    public void setState(double x, double y) {
        if ((this.x == null || this.x != x) || (this.y == null || this.y != y)) {
            this.x = x;
            this.y = y;
            GL11.glScaled(x, y, 1.0);
        }
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }
}
