package net.ilexiconn.pixle.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import net.ilexiconn.pixle.client.config.ClientConfig;
import net.ilexiconn.pixle.client.event.RenderEvent;
import net.ilexiconn.pixle.client.gl.GLStateManager;
import net.ilexiconn.pixle.client.gui.GUI;
import net.ilexiconn.pixle.client.gui.WorldGUI;
import net.ilexiconn.pixle.client.render.TextureManager;
import net.ilexiconn.pixle.entity.PlayerEntity;
import net.ilexiconn.pixle.event.bus.EventBus;
import net.ilexiconn.pixle.event.event.PixleInitializeEvent;
import net.ilexiconn.pixle.level.ClientLevel;
import net.ilexiconn.pixle.network.ConnectPacket;
import net.ilexiconn.pixle.network.PixleNetworkManager;
import net.ilexiconn.pixle.network.PixlePacket;
import net.ilexiconn.pixle.network.PlayerMovePacket;
import net.ilexiconn.pixle.plugin.PluginJson;
import net.ilexiconn.pixle.server.PixleServer;
import net.ilexiconn.pixle.util.ConfigUtils;
import net.ilexiconn.pixle.util.CrashReport;
import net.ilexiconn.pixle.util.SystemUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PixleClient extends Listener {
    public List<PluginJson> pluginList = new ArrayList<>();

    private boolean closeRequested;
    private int fps;
    private double delta;

    private TextureManager textureManager;

    private List<GUI> openGUIs = new ArrayList<>();

    private Client client;

    private ClientLevel level;
    private PlayerEntity player;

    private String username;

    public static PixleClient INSTANCE;

    public File configFile = new File(SystemUtils.getGameFolder(), "config.json");
    public ClientConfig config;

    private TrueTypeFont fontRenderer;

    private boolean hasIntegratedServer;
    private PixleServer integratedServer;

    public void start(Map<String, String> properties) {
        try {
            PixleClient.INSTANCE = this;
            config = ConfigUtils.loadConfig(configFile, ClientConfig.class);
            this.username = properties.get("username");
            init();
            int port = properties.containsKey("port") ? Integer.parseInt(properties.get("port")) : 25565;
            if (properties.containsKey("host")) {
                connect(properties.get("host"), port);
            } else {
                Log.info("Client", "Starting integrated server on port " + port);
                hasIntegratedServer = true;
                integratedServer = new PixleServer();
                integratedServer.pluginList = pluginList;
                new Thread("Server") {
                    @Override
                    public void run() {
                        integratedServer.start(port);
                    }
                }.start();
                connect("localhost", port);
            }

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

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LIGHTING);

            GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GL11.glClearDepth(1);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            try {
                InputStream inputStream = PixleClient.class.getResourceAsStream("/font/pixel-love.ttf");

                Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
                awtFont = awtFont.deriveFont(24F);
                fontRenderer = new TrueTypeFont(awtFont, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (!Display.isCloseRequested() && !isCloseRequested()) {
                if (Display.wasResized()) {
                    int width = Display.getWidth();
                    int height = Display.getHeight();
                    GL11.glMatrixMode(GL11.GL_PROJECTION);
                    GL11.glLoadIdentity();
                    GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glScissor(0, 0, width, height);
                    GL11.glViewport(0, 0, width, height);
                }

                long currentTime = System.nanoTime();
                delta += (currentTime - previousTime) / nanoUpdates;
                previousTime = currentTime;

                while (delta >= 1) {
                    tick();

                    delta--;
                    ups++;
                }

                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
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

    private void init() throws IOException {
        textureManager = new TextureManager();
        level = new ClientLevel();
        client = new Client(32767, 32767);
        client.start();
        PixleNetworkManager.init(client);
        openGUI(new WorldGUI(this));

        EventBus.get().post(new PixleInitializeEvent());
    }

    public void connect(String host, int port) throws IOException {
        client.connect(5000, host, port);
        client.addListener(this);
    }

    public void disconnect() {
        if (client != null) {
            client.close();
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
                client.sendTCP(new PlayerMovePacket(player, moveX, jumping));
            }
        }

        level.update();
    }

    private void render() {
        if (EventBus.get().post(new RenderEvent.Pre(this))) {
            GLStateManager.enableColor();
            new ArrayList<>(getOpenGUIs()).forEach(GUI::render);
        }
        EventBus.get().post(new RenderEvent.Post(this));
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
    public void connected(Connection connection) {
        client.sendTCP(new ConnectPacket(username));
    }

    @Override
    public void disconnected(Connection connection) {
        System.exit(1); //bye world
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof PixlePacket) {
            PixlePacket packet = (PixlePacket) object;
            packet.handleClient(connection);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setPlayer(int player) {
        this.player = (PlayerEntity) level.getEntityById(player);
    }

    public TrueTypeFont getFontRenderer() {
        return fontRenderer;
    }

    public boolean hasIntegratedServer() {
        return hasIntegratedServer;
    }

    public PixleServer getIntegratedServer() {
        return integratedServer;
    }
}

