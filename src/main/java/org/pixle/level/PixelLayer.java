package org.pixle.level;

public enum PixelLayer {
    FOREGROUND(0), BACKGROUND(-30);

    int colorOffset;

    PixelLayer(int colorOffset) {
        this.colorOffset = colorOffset;
    }

    public int getColorOffset() {
        return colorOffset;
    }
}
