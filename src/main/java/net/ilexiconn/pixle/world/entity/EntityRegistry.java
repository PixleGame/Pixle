package net.ilexiconn.pixle.world.entity;

import net.ilexiconn.pixle.world.World;

import java.util.HashMap;
import java.util.Map;

public class EntityRegistry {
    private static Map<Integer, Class<? extends Entity>> entityMap = new HashMap<>();

    public static void register() {
        registerEntity(0, PlayerEntity.class);
    }

    public static void registerEntity(int id, Class<? extends Entity> clazz) {
        entityMap.put(id, clazz);
    }

    public static Entity initializeEntity(int id, World world) {
        try {
            return entityMap.get(id).getConstructor(World.class).newInstance(world);
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
