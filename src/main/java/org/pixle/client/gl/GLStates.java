package org.pixle.client.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.pixle.client.gl.state.BooleanState;
import org.pixle.client.gl.state.ColorState;
import org.pixle.client.gl.state.ScaleState;

public class GLStates {
    public BooleanState BLEND = new BooleanState(GL11.GL_BLEND);
    public ColorState COLOR = new ColorState();
    public BooleanState TEXTURE = new BooleanState(GL11.GL_TEXTURE_2D);
    public ScaleState SCALE = new ScaleState();
    public BooleanState RESCALE_NORMAL = new BooleanState(GL12.GL_RESCALE_NORMAL);

    private String tag;

    public GLStates(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
