package net.ilexiconn.pixle.client;

import net.ilexiconn.netconn.client.Client;
import net.ilexiconn.pixle.client.gui.GUI;
import net.ilexiconn.pixle.client.gui.WorldGUI;
import net.ilexiconn.pixle.client.render.TextureManager;
import net.ilexiconn.pixle.crash.CrashReport;
import net.ilexiconn.pixle.world.World;
import net.ilexiconn.pixle.world.entity.PlayerEntity;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PixleClient {
    public static final int SCREEN_WIDTH = 854;
    public static final int SCREEN_HEIGHT = 480;

    private boolean closeRequested;
    private int fps;
    private double delta;

    private TextureManager textureManager;

    private List<GUI> openGUIs = new ArrayList<>();

    private Client client;

    private World world;
    private PlayerEntity player;

    public void start() {
        try {
            init();
            try {
                Display.setDisplayMode(new DisplayMode(PixleClient.SCREEN_WIDTH, PixleClient.SCREEN_HEIGHT));
                Display.setTitle("Pixle");
                Display.setResizable(true);
                Display.create();
                Keyboard.create();
                Mouse.create();
            } catch (LWJGLException e) {
                e.printStackTrace();
            }

            delta = 0;
            long previousTime = System.nanoTime();
            long timer = System.currentTimeMillis();
            int ups = 0;
            double nanoUpdates = 1000000000.0 / 60.0;

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, PixleClient.SCREEN_WIDTH, 0, PixleClient.SCREEN_HEIGHT, 1, -1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);

            while (!Display.isCloseRequested() && !isCloseRequested()) {
                long currentTime = System.nanoTime();
                delta += (currentTime - previousTime) / nanoUpdates;
                previousTime = currentTime;

                while (delta >= 1) {
                    tick();

                    delta--;
                    ups++;
                }

                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                GL11.glPushMatrix();

                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                render();

                fps++;

                if (System.currentTimeMillis() - timer > 1000) {
                    Display.setTitle("Pixle - FPS: " + fps + " - UPS: " + ups);
                    fps = 0;

                    timer += 1000;
                    ups = 0;
                }

                GL11.glPopMatrix();

                Display.update();
            }

            System.exit(-1);
        } catch (Exception e) {
            System.err.println(CrashReport.makeCrashReport(e, "An unexpected error occurred."));
        }
    }

    private void init() {
        textureManager = new TextureManager();
        openGUI(new WorldGUI(this));

        world = new World();
        player = new PlayerEntity(world);
        world.addEntity(player);
    }

    public void connect(String host, int port) throws IOException {
        client = new Client(host, port);
    }

    public void disconnect() {
        if (client != null) {
            client.disconnect();
            client = null;
        }
    }

    public void stop() {
        closeRequested = true;
    }

    private void tick() {
        float moveX = 0.0F;

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            moveX = -1.0F;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            moveX = 1.0F;
        }

        player.velX = moveX;

        world.update();
    }

    private void render() {
        for (GUI gui : new ArrayList<>(getOpenGUIs())) {
            gui.render();
        }
    }

    public void openGUI(GUI gui) {
        if (!openGUIs.contains(gui)) {
            openGUIs.add(gui);
        }
    }

    public void closeGUI(GUI gui) {
        openGUIs.remove(gui);
    }

    public List<GUI> getOpenGUIs() {
        return openGUIs;
    }

    public boolean isCloseRequested() {
        return closeRequested;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    public World getWorld() {
        return world;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public double getDelta() {
        return delta;
    }
}

