package net.ilexiconn.pixle.client.render.entity;

import net.ilexiconn.pixle.world.World;
import net.ilexiconn.pixle.world.entity.Entity;

public interface IEntityRenderer<T extends Entity> {
    void render(T entity, World world, float delta);
}
