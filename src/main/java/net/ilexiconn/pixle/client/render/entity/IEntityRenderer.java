package net.ilexiconn.pixle.client.render.entity;

import net.ilexiconn.pixle.level.Level;
import net.ilexiconn.pixle.entity.Entity;

public interface IEntityRenderer<T extends Entity> {
    void render(T entity, int x, int y, Level level, float delta);
}
