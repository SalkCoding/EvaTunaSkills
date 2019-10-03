package net.alkaonline.alkaskills.util;

import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

public class AdditionalAttack implements Runnable {
    private BukkitTask task;

    private LivingEntity entity;
    private int repeat;

    public AdditionalAttack(LivingEntity entity, int repeat) {
        this.entity = entity;
        this.repeat = repeat;
    }

    public void setTask(BukkitTask task) {
        this.task = task;
    }

    private int count = 0;

    @Override
    public void run() {
        if (count == repeat || entity.isDead()) {
            task.cancel();
            return;
        }
        Location location = entity.getLocation();
        location.setY(location.getY() + 1);
        World world = location.getWorld();
        world.playSound(location, Sound.ENTITY_PLAYER_HURT, 0.5f, 1f);
        world.spawnParticle(Particle.CRIT, location, 10);
        entity.playEffect(EntityEffect.HURT);
        //((CraftLivingEntity) entity).getHandle().damageEntity(DamageSource.OUT_OF_WORLD, 1);
        count++;
    }
}
