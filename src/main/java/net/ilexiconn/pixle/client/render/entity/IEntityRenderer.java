package net.ilexiconn.pixle.client.render.entity;

import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.level.Level;

public interface IEntityRenderer<T extends Entity> {
    void render(T entity, int x, int y, Level level, float delta);
}
