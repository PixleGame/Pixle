package net.ilexiconn.pixle.client;

import net.ilexiconn.netconn.client.Client;
import net.ilexiconn.pixle.Materials;
import net.ilexiconn.pixle.client.gui.BaseGUI;
import net.ilexiconn.pixle.client.gui.WorldGUI;
import net.ilexiconn.pixle.client.render.PixleRenderHandler;
import net.ilexiconn.pixle.client.render.TextureManager;
import net.ilexiconn.pixle.crash.CrashReport;
import net.ilexiconn.pixle.world.World;
import net.ilexiconn.pixle.world.entity.EntityRegistry;
import net.ilexiconn.pixle.world.entity.PlayerEntity;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class PixleClient {
    public static final int SCREEN_WIDTH = 854;
    public static final int SCREEN_HEIGHT = 480;

    private boolean closeRequested;

    private PixleRenderHandler renderHandler;
    private TextureManager textureManager;

    private BaseGUI openGUI;
    private WorldGUI worldGUI;

    private Client client;

    private World world;
    private PlayerEntity player;

    public void start() {
        try {
            init();
            startTick();
        } catch (Exception e) {
            System.err.println(CrashReport.makeCrashReport(e, "An unexpected error occurred."));
        }
    }

    private void init() {
        textureManager = new TextureManager();
        worldGUI = new WorldGUI(this);

        Materials.init();
        EntityRegistry.register();

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

    private void startTick() {
        renderHandler = new PixleRenderHandler(this);
        renderHandler.start();

        double delta = 0;
        long previousTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        int ups = 0;
        double nanoUpdates = 1000000000.0 / 60.0;

        while (!isCloseRequested()) {
            long currentTime = System.nanoTime();
            delta += (currentTime - previousTime) / nanoUpdates;
            previousTime = currentTime;

            while (delta >= 1) {
                tick();

                delta--;
                ups++;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                System.out.println("UPS: " + ups);

                timer += 1000;
                ups = 0;
            }
        }

        System.exit(-1);
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

    public void openGUI(BaseGUI gui) {
        openGUI = gui;
    }

    public BaseGUI getOpenGUI() {
        return openGUI;
    }

    public boolean isCloseRequested() {
        return closeRequested;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    public WorldGUI getWorldGUI() {
        return worldGUI;
    }

    public World getWorld() {
        return world;
    }
}
