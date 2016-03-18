package org.pixle.client.render;

import org.pixle.client.render.entity.IEntityRenderer;
import org.pixle.client.render.entity.PixelEntityRenderer;
import org.pixle.client.render.entity.PlayerEntityRenderer;
import org.pixle.entity.Entity;
import org.pixle.entity.PixelEntity;
import org.pixle.entity.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class RenderingRegistry {
    private static Map<Class<? extends Entity>, IEntityRenderer<? extends Entity>> rendererMap = new HashMap<>();

    static {
        rendererMap.put(PlayerEntity.class, new PlayerEntityRenderer());
        rendererMap.put(PixelEntity.class, new PixelEntityRenderer());
    }

    public static <E extends Entity> IEntityRenderer<E> getEntityRenderer(Class<E> entityClass) {
        if (rendererMap.containsKey(entityClass)) {
            return (IEntityRenderer<E>) rendererMap.get(entityClass);
        } else {
            return null;
        }
    }
}
