package net.ilexiconn.pixle.client.render;

import net.ilexiconn.pixle.client.render.entity.IEntityRenderer;
import net.ilexiconn.pixle.client.render.entity.PlayerEntityRenderer;
import net.ilexiconn.pixle.entity.Entity;
import net.ilexiconn.pixle.entity.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class RenderingRegistry {
    private static Map<Class<? extends Entity>, IEntityRenderer<? extends Entity>> rendererMap = new HashMap<>();

    static {
        rendererMap.put(PlayerEntity.class, new PlayerEntityRenderer());
    }

    public static <E extends Entity> IEntityRenderer<E> getEntityRenderer(Class<E> entityClass) {
        if (rendererMap.containsKey(entityClass)) {
            return (IEntityRenderer<E>) rendererMap.get(entityClass);
        } else {
            return null;
        }
    }
}
