package net.ilexiconn.pixle.client;

import net.ilexiconn.netconn.client.Client;
import net.ilexiconn.pixle.client.gl.GLStateManager;
import net.ilexiconn.pixle.client.gui.GUI;
import net.ilexiconn.pixle.client.gui.WorldGUI;
import net.ilexiconn.pixle.client.render.TextureManager;
import net.ilexiconn.pixle.crash.CrashReport;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.event.bus.EventBus;
import net.ilexiconn.pixle.event.bus.EventHandler;
import net.ilexiconn.pixle.event.event.InitializeEvent;
import net.ilexiconn.pixle.level.Level;
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
    private boolean closeRequested;
    private int fps;
    private double delta;

    private TextureManager textureManager;

    private List<GUI> openGUIs = new ArrayList<>();

    private Client client;

    private Level level;
    private PlayerEntity player;

    public static final EventBus EVENT_BUS = new EventBus();

    public void start() {
        try {
            init();
            try {
                Display.setDisplayMode(new DisplayMode(854, 480));
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
            GL11.glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 1, -1);
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
                GLStateManager.pushMatrix();

                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                render();

                fps++;

                if (System.currentTimeMillis() - timer > 1000) {
                    Display.setTitle("Pixle - FPS: " + fps + " - UPS: " + ups);
                    fps = 0;

                    timer += 1000;
                    ups = 0;
                }

                GLStateManager.popMatrix();

                Display.update();
            }

            System.exit(-1);
        } catch (Exception e) {
            System.err.println(CrashReport.makeCrashReport(e, e.getLocalizedMessage()));
        }
    }

    private void init() {
        textureManager = new TextureManager();
        openGUI(new WorldGUI(this));

        level = new Level();
        player = new PlayerEntity(level);
        player.posY = level.getHeight((int) player.posX) + 1;
        level.addEntity(player);

        PixleClient.EVENT_BUS.register(this);
        PixleClient.EVENT_BUS.post(new InitializeEvent());
    }

    @EventHandler
    public void onInitialize(InitializeEvent event) {
        System.out.println("Event bus test!");
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
            moveX = -0.25F;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            moveX = 0.25F;
        }
        if (player.onSurface) {
            if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                player.velY = 1.0F;
            }
        }
        player.velX = moveX;

        level.update();
    }

    private void render() {
        GLStateManager.enableColor();
        new ArrayList<>(getOpenGUIs()).forEach(GUI::render);
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

    public Level getLevel() {
        return level;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public double getDelta() {
        return delta;
    }
}

