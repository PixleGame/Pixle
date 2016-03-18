package org.pixle.entity;

import org.pixle.level.Level;

import java.util.HashMap;
import java.util.Map;

public class EntityRegistry {
    private static Map<Integer, Class<? extends Entity>> entityMap = new HashMap<>();

    static {
        registerEntity(0, PlayerEntity.class);
        registerEntity(1, PixelEntity.class);
    }

    public static void registerEntity(int id, Class<? extends Entity> clazz) {
        entityMap.put(id, clazz);
    }

    public static Entity initializeEntity(int id, Level level) {
        try {
            return entityMap.get(id).getConstructor(Level.class).newInstance(level);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getEntityID(Class<? extends Entity> entityClass) {
        for (Map.Entry<Integer, Class<? extends Entity>> entry : entityMap.entrySet()) {
            if (entry.getValue().equals(entityClass)) {
                return entry.getKey();
            }
        }
        return -1;
    }
}
