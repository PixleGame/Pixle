package org.pixle.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.pixle.client.config.ClientConfig;
import org.pixle.client.event.GUIInitializationEvent;
import org.pixle.client.event.RenderEvent;
import org.pixle.client.gl.GLStateManager;
import org.pixle.client.gui.GUI;
import org.pixle.client.gui.MainMenuGUI;
import org.pixle.client.gui.RenderResolution;
import org.pixle.client.render.TextureManager;
import org.pixle.entity.PlayerEntity;
import org.pixle.event.PixleInitializeEvent;
import org.pixle.event.bus.EventBus;
import org.pixle.level.ClientLevel;
import org.pixle.level.Level;
import org.pixle.network.ConnectPacket;
import org.pixle.network.PixleNetworkManager;
import org.pixle.network.PixlePacket;
import org.pixle.network.PlayerMovePacket;
import org.pixle.plugin.PluginContainer;
import org.pixle.server.PixleServer;
import org.pixle.util.ConfigUtils;
import org.pixle.util.CrashReport;
import org.pixle.util.SystemUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PixleClient extends Listener {
    public static PixleClient INSTANCE;
    public List<PluginContainer> pluginList = new ArrayList<>();
    public File configFile = new File(SystemUtils.getGameFolder(), "config.json");
    public ClientConfig config;
    private boolean closeRequested;
    private int fps;
    private double delta;
    private TextureManager textureManager;
    private List<GUI> openGUIs = new ArrayList<>();
    private Client client;
    private ClientLevel level;
    private PlayerEntity player;
    private String username;
    private TrueTypeFont fontRenderer;

    private boolean hasIntegratedServer;
    private PixleServer integratedServer;

    private String host;
    private int port = 25565;

    private RenderResolution renderResolution;

    public void start(Map<String, String> properties) {
        try {
            PixleClient.INSTANCE = this;
            config = ConfigUtils.loadConfig(configFile, ClientConfig.class);
            this.username = properties.get("username");
            renderResolution = new RenderResolution();
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

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LIGHTING);

            GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GL11.glClearDepth(1);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            try {
                InputStream inputStream = PixleClient.class.getResourceAsStream("/font/00TT.ttf");

                Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
                awtFont = awtFont.deriveFont(11.0F);
                fontRenderer = new TrueTypeFont(awtFont, false);
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
                    renderResolution = new RenderResolution();
                    for (GUI gui : getOpenGUIs()) {
                        gui.clearComponents();
                        gui.updateComponents(renderResolution);
                    }
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

    public void startGame() {
        try {
            if (host != null) {
                connect(host, port);
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
        } catch (Exception e) {
            System.err.println(CrashReport.makeCrashReport(e, e.getLocalizedMessage()));
        }
    }

    public void setServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void init() throws IOException {
        textureManager = new TextureManager();
        level = new ClientLevel();
        client = new Client(32767, 32767);
        client.start();
        PixleNetworkManager.init(client);
        openGUI(new MainMenuGUI());

        EventBus.INSTANCE.post(new PixleInitializeEvent());
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
                player.moveX = moveX;
                player.jumping = jumping;
                client.sendTCP(new PlayerMovePacket(player, moveX, jumping));
            }
        }
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                char c = Keyboard.getEventCharacter();
                int key = Keyboard.getEventKey();
                for (GUI gui : getOpenGUIs()) {
                    gui.keyTyped(c, key);
                }
            }
        }
        int mouseX = Mouse.getX();
        int mouseY = Display.getHeight() - Mouse.getY();
        mouseX /= renderResolution.getScale();
        mouseY /= renderResolution.getScale();
        while (Mouse.next()) {
            int button = Mouse.getEventButton();
            if (Mouse.getEventButtonState()) {
                for (GUI gui : getOpenGUIs()) {
                    gui.mouseClicked(mouseX, mouseY, button);
                }
            }
            if (Mouse.isButtonDown(button)) {
                for (GUI gui : getOpenGUIs()) {
                    gui.mouseDown(mouseX, mouseY, button);
                }
            }
        }
        level.update();
        openGUIs.forEach(GUI::tick);
    }

    private void render() {
        renderResolution.applyScale();
        if (EventBus.INSTANCE.post(new RenderEvent.Pre())) {
            GLStateManager.enableColor();
            int mouseX = Mouse.getX();
            int mouseY = Display.getHeight() - Mouse.getY();
            mouseX /= renderResolution.getScale();
            mouseY /= renderResolution.getScale();
            for (GUI gui : getOpenGUIs()) {
                gui.render(mouseX, mouseY);
            }
            EventBus.INSTANCE.post(new RenderEvent.Post());
        }
    }

    public void openGUI(GUI gui) {
        GUIInitializationEvent event = new GUIInitializationEvent.Pre(gui);
        if (EventBus.INSTANCE.post(event)) {
            gui = event.getGUI();
            gui.clearComponents();
            gui.updateComponents(renderResolution);
            if (!openGUIs.contains(gui)) {
                openGUIs.add(gui);
            }
            EventBus.INSTANCE.post(new GUIInitializationEvent.Post(gui));
        }
    }

    public void closeGUI(GUI gui) {
        openGUIs.remove(gui);
    }

    public List<GUI> getOpenGUIs() {
        return new ArrayList<>(openGUIs);
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

    public void setPlayer(int player) {
        this.player = (PlayerEntity) level.getEntityById(player);
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

    public TrueTypeFont getFontRenderer() {
        return fontRenderer;
    }

    public boolean hasIntegratedServer() {
        return hasIntegratedServer;
    }

    public PixleServer getIntegratedServer() {
        return integratedServer;
    }

    public RenderResolution getRenderResolution() {
        return renderResolution;
    }

    public int getSelectionX(int mouseX) {
        int width = renderResolution.getWidth();
        int centerX = width / 2;
        return (int) (((mouseX - centerX) / Level.PIXEL_SIZE) + player.posX);
    }

    public int getSelectionY(int mouseY) {
        int height = renderResolution.getHeight();
        int centerY = height / 2;
        return (int) (((height - mouseY - centerY) / Level.PIXEL_SIZE) + player.posY);
    }
}

