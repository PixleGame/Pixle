package org.pixle.client.render.entity;

import org.pixle.entity.Entity;
import org.pixle.level.Level;

public interface IEntityRenderer<T extends Entity> {
    void render(T entity, int x, int y, Level level, float delta);
}
