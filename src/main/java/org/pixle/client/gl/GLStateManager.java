package org.pixle.client.gl;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

public class GLStateManager {
    private static BooleanState blendState = new BooleanState(GL11.GL_BLEND);
    private static ColorState colorState = new ColorState();
    private static BooleanState textureState = new BooleanState(GL11.GL_TEXTURE_2D);
    private static ScaleState scaleState = new ScaleState();
    private static BooleanState rescaleNormalState = new BooleanState(0x803A);

    public static void enableBlend() {
        blendState.setEnabled();
    }

    public static void disableBlend() {
        blendState.setDisabled();
    }

    public static void setColor(int hex) {
        setColor(((hex & 0xFF0000) >> 16) / 255.0F, ((hex & 0xFF00) >> 8) / 255.0F, (hex & 0xFF) / 255.0F);
    }

    public static void setColor(float red, float green, float blue) {
        setColor(red, green, blue, colorState.alpha == null ? 1.0F : colorState.alpha);
    }

    public static void setColor(float red, float green, float blue, float alpha) {
        colorState.setState(red, green, blue, alpha);
    }

    public static void enableColor() {
        colorState.setEnabled();
    }

    public static void disableColor() {
        colorState.setDisabled();
    }

    public static void enableTexture() {
        textureState.setEnabled();
    }

    public static void disableTexture() {
        textureState.setDisabled();
    }

    public static void disableRescaleNormal() {
        rescaleNormalState.setDisabled();
    }

    public static void enableRescaleNormal() {
        rescaleNormalState.setEnabled();
    }

    public static void scale(double x, double y) {
        scaleState.setState(x, y);
    }

    public static void scale(float x, float y) {
        scale((double) x, (double) y);
    }

    public static void startDrawingQuads() {
        GL11.glBegin(GL11.GL_QUADS);
    }

    public static void end() {
        GL11.glEnd();
    }

    public static void pushMatrix() {
        GL11.glPushMatrix();
    }

    public static void popMatrix() {
        GL11.glPopMatrix();
        blendState.reset();
        colorState.reset();
        textureState.reset();
        scaleState.reset();
        rescaleNormalState.reset();
    }

    public static Color getColor() {
        return new Color(colorState.red, colorState.green, colorState.blue);
    }

    static class BooleanState {
        private int capability;
        private Boolean currentState;

        public BooleanState(int capability) {
            this.capability = capability;
        }

        public void setEnabled() {
            setState(true);
        }

        public void setDisabled() {
            setState(false);
        }

        private void setState(boolean state) {
            if (currentState == null || state != currentState) {
                currentState = state;
                if (state) {
                    GL11.glEnable(capability);
                } else {
                    GL11.glDisable(capability);
                }
            }
        }

        public void reset() {
            currentState = null;
        }
    }

    static class ColorState extends BooleanState {
        private Float red;
        private Float green;
        private Float blue;
        private Float alpha;

        public ColorState() {
            super(GL11.GL_COLOR);
        }

        public void setState(float red, float green, float blue, float alpha) {
            if ((this.red == null || this.red != red) || (this.green == null || this.green != green) || (this.blue == null || this.blue != blue) || (this.alpha == null || this.alpha != alpha)) {
                this.red = red;
                this.green = green;
                this.blue = blue;
                this.alpha = alpha;
                GL11.glColor4f(red, green, blue, alpha);
            }
        }

        @Override
        public void reset() {
            super.reset();
            red = null;
            green = null;
            blue = null;
            alpha = null;
        }
    }

    static class ScaleState {
        private Double x;
        private Double y;

        public void setState(double x, double y) {
            if ((this.x == null || this.x != x) || (this.y == null || this.y != y)) {
                this.x = x;
                this.y = y;
                GL11.glScaled(x, y, 1.0D);
            }
        }

        public void reset() {
            x = null;
            y = null;
        }
    }
}
