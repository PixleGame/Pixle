package org.pixle.level;

public enum PixelLayer {
    FOREGROUND(0), BACKGROUND(-30);

    public static PixelLayer[] renderLayers = new PixelLayer[] { BACKGROUND, FOREGROUND };

    int colorOffset;

    PixelLayer(int colorOffset) {
        this.colorOffset = colorOffset;
    }

    public int getColorOffset() {
        return colorOffset;
    }
}
