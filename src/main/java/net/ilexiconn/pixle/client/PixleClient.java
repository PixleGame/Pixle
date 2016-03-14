package net.ilexiconn.pixle.client;

import net.ilexiconn.netconn.client.Client;
import net.ilexiconn.netconn.client.IClientListener;
import net.ilexiconn.pixle.client.gl.GLStateManager;
import net.ilexiconn.pixle.client.gui.GUI;
import net.ilexiconn.pixle.client.gui.WorldGUI;
import net.ilexiconn.pixle.client.render.TextureManager;
import net.ilexiconn.pixle.client.config.ClientConfig;
import net.ilexiconn.pixle.crash.CrashReport;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.event.bus.EventBus;
import net.ilexiconn.pixle.event.bus.EventHandler;
import net.ilexiconn.pixle.event.event.InitializeEvent;
import net.ilexiconn.pixle.level.ClientLevel;
import net.ilexiconn.pixle.network.ConnectPacket;
import net.ilexiconn.pixle.network.PixleNetworkManager;
import net.ilexiconn.pixle.network.PlayerMovePacket;
import net.ilexiconn.pixle.util.ConfigUtils;
import net.ilexiconn.pixle.util.SystemUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PixleClient implements IClientListener {
    private boolean closeRequested;
    private int fps;
    private double delta;

    private TextureManager textureManager;

    private List<GUI> openGUIs = new ArrayList<>();

    private Client client;

    private ClientLevel level;
    private PlayerEntity player;

    private String username;

    public static final EventBus EVENT_BUS = new EventBus();

    public static PixleClient INSTANCE;

    public File configFile = new File(SystemUtils.getGameFolder(), "config.json");
    public ClientConfig config;

    public void start(String username, String host, int port) {
        try {
            PixleClient.INSTANCE = this;
            config = ConfigUtils.loadConfig(configFile, ClientConfig.class);
            this.username = username;
            init();
            connect(host, port);
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
                if (config.maxFPS != -1) {
                    Display.sync(config.maxFPS);
                }
            }

            System.exit(-1);
        } catch (Exception e) {
            System.err.println(CrashReport.makeCrashReport(e, e.getLocalizedMessage()));
        }
    }

    private void init() {
        textureManager = new TextureManager();
        level = new ClientLevel();
        PixleNetworkManager.init();
        openGUI(new WorldGUI(this));

        PixleClient.EVENT_BUS.register(this);
        PixleClient.EVENT_BUS.post(new InitializeEvent());
    }

    @EventHandler
    public void onInitialize(InitializeEvent event) {
        System.out.println("Event bus test!");
    }

    public void connect(String host, int port) throws IOException {
        client = new Client(host, port);
        client.addListener(this);

        new Thread(() -> {
            while (client.isRunning()) {
                client.listen();
            }
        }).start();
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
        if (player != null) {
            float moveX = 0.0F;
            boolean jumping = false;
            if (Keyboard.isKeyDown(config.keyLeft)) {
                moveX = -0.25F;
            } else if (Keyboard.isKeyDown(config.keyRight)) {
                moveX = 0.25F;
            }
            if (Keyboard.isKeyDown(config.keyJump)) {
                jumping = true;
            }
            if (jumping != player.jumping || moveX != player.moveX) {
                client.sendPacketToServer(new PlayerMovePacket(player, moveX, jumping));
            }
        }

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

    public ClientLevel getLevel() {
        return level;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public double getDelta() {
        return delta;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void onConnected(Client client, Socket socket) {
        client.sendPacketToServer(new ConnectPacket(username));
    }

    @Override
    public void onDisconnected(Client client) {

    }

    public String getUsername() {
        return username;
    }

    public void setPlayer(int player) {
        this.player = (PlayerEntity) level.getEntityById(player);
    }
}

