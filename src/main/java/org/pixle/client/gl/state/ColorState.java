package org.pixle.client.gl.state;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

public class ColorState extends BooleanState {
    private Float red;
    private Float green;
    private Float blue;
    private Color color;

    public ColorState() {
        super(GL11.GL_COLOR);
    }

    public void setState(float red, float green, float blue) {
        if ((this.red == null || this.red != red) || (this.green == null || this.green != green) || (this.blue == null || this.blue != blue)) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.color = new Color(red, green, blue);
            GL11.glColor4f(red, green, blue, 1.0F);
        }
    }

    public Float getRed() {
        return red;
    }

    public Float getGreen() {
        return green;
    }

    public Float getBlue() {
        return blue;
    }

    public Color getColor() {
        return color;
    }
}
