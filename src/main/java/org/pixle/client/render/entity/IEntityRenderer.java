package org.pixle.client.render.entity;

import org.pixle.entity.Entity;
import org.pixle.level.Level;

public interface IEntityRenderer<T extends Entity> {
    void render(T entity, float x, float y, Level level, float delta);
}
