package org.pixle.client.gl;

import com.esotericsoftware.minlog.Log;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.List;

public class GLStateManager {
    private static List<GLStates> stateList = new ArrayList<>();
    private static GLStates currentState;

    static {
        stateList.add(new GLStates("GLOBAL"));
        currentState = stateList.get(0);
    }
    
    public static void enableBlend() {
        Log.debug("GLStateManager", "Enabling BLEND");
        currentState.BLEND.setEnabled();
    }

    public static void disableBlend() {
        Log.debug("GLStateManager", "Disabling BLEND");
        currentState.BLEND.setDisabled();
    }

    public static void setColor(int hex) {
        setColor(((hex & 0xFF0000) >> 16) / 255.0F, ((hex & 0xFF00) >> 8) / 255.0F, (hex & 0xFF) / 255.0F);
    }

    public static void setColor(float red, float green, float blue) {
        Log.debug("GLStateManager", "Setting COLOR to " + red + "," + green + "," + blue);
        currentState.COLOR.setState(red, green, blue);
    }

    public static void enableColor() {
        Log.debug("GLStateManager", "Enabling COLOR");
        currentState.COLOR.setEnabled();
    }

    public static void disableColor() {
        Log.debug("GLStateManager", "Disabling COLOR");
        currentState.COLOR.setDisabled();
    }

    public static void enableTexture() {
        Log.debug("GLStateManager", "Enabling TEXTURE");
        currentState.TEXTURE.setEnabled();
    }

    public static void disableTexture() {
        Log.debug("GLStateManager", "Disabling TEXTURE");
        currentState.TEXTURE.setDisabled();
    }

    public static void enableRescaleNormal() {
        Log.debug("GLStateManager", "Enabling RESCALE_NORMAL");
        currentState.RESCALE_NORMAL.setEnabled();
    }

    public static void disableRescaleNormal() {
        Log.debug("GLStateManager", "Disabling RESCALE_NORMAL");
        currentState.RESCALE_NORMAL.setDisabled();
    }

    public static void scale(double x, double y) {
        Log.debug("GLStateManager", "Setting SCALE to " + x + "," + y);
        currentState.SCALE.setState(x, y);
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
        GLStates matrixState = new GLStates("MATRIX_" + stateList.size());
        stateList.add(matrixState);
        currentState = matrixState;
        Log.debug("GLStateManager", "Pushing " + currentState.getTag());
    }

    public static void popMatrix() {
        GL11.glPopMatrix();
        GLStates newState = stateList.get(stateList.indexOf(currentState) - 1);
        Log.debug("GLStateManager", "Popping " + currentState.getTag());
        stateList.remove(currentState);
        currentState = newState;
        Log.debug("GLStateManager", "Pushing " + currentState.getTag());
    }

    public static Color getColor() {
        return currentState.COLOR.getColor();
    }

    public static GLStates getCurrentState() {
        return currentState;
    }
}
