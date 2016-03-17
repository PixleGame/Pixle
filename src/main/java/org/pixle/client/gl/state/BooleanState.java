package org.pixle.client.gl.state;

import org.lwjgl.opengl.GL11;

public class BooleanState {
    private int capability;
    private Boolean state;

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
        if (this.state == null || state != this.state) {
            this.state = state;
            if (state) {
                GL11.glEnable(capability);
            } else {
                GL11.glDisable(capability);
            }
        }
    }

    public int getCapability() {
        return capability;
    }

    public Boolean getState() {
        return state;
    }
}
