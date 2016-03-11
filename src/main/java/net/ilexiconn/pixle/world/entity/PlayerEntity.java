package net.ilexiconn.pixle.world.entity;

import net.ilexiconn.pixle.world.World;

public class PlayerEntity extends Entity {
    public PlayerEntity(World world) {
        super(world);
        this.setBounds(1.0F, 2.0F);
    }
}
