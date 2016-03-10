package net.ilexiconn.pixle.client.gui;

import net.ilexiconn.pixle.client.PixleClient;
import net.ilexiconn.pixle.client.render.RenderingRegistry;
import net.ilexiconn.pixle.client.render.entity.IEntityRenderer;
import net.ilexiconn.pixle.world.World;
import net.ilexiconn.pixle.world.entity.Entity;

public class WorldGUI extends GUI {
    public WorldGUI(PixleClient pixle) {
        super(pixle);
    }

    @Override
    public void render() {
        World world = pixle.getWorld();

        for (Entity entity : world.getEntities()) {
            IEntityRenderer entityRenderer = RenderingRegistry.getEntityRenderer(entity.getClass());
            if (entityRenderer != null) {
                entityRenderer.render(entity, world, (float) pixle.getDelta());
            }
        }
    }
}
