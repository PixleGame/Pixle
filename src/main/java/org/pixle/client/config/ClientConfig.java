package org.pixle.client.config;

import org.lwjgl.input.Keyboard;

public class ClientConfig {
    @ConfigOption(name = "Max FPS", type = OptionType.SLIDER, minValue = -1, maxValue = 60)
    public int maxFPS = -1;

    @ConfigOption(name = "Move Left Key", type = OptionType.KEY)
    public int keyLeft = Keyboard.KEY_A;

    @ConfigOption(name = "Move Right Key", type = OptionType.KEY)
    public int keyRight = Keyboard.KEY_D;

    @ConfigOption(name = "Jump Key", type = OptionType.KEY)
    public int keyJump = Keyboard.KEY_SPACE;
}
