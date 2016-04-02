package org.pixle.level;

public enum PixelLayer {
    FOREGROUND(0), BACKGROUND(-30);

    float colorOffset;

    PixelLayer(int colorOffset) {
        this.colorOffset = colorOffset / 255.0F;
    }

    public float getColorOffset() {
        return colorOffset;
    }
}
