package com.ar.askgaming.nightmare.Listeners;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.ar.askgaming.nightmare.NightMare;
import com.ar.askgaming.nightmare.Types.BloodMoon;
import com.ar.askgaming.nightmare.Types.HeadLessHorseMan;

public class CreatureSpawnListener implements Listener{

    private final NightMare plugin;

    public CreatureSpawnListener() {
        this.plugin = NightMare.getInstance();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Enemy)){
            return;
        }
        
        BloodMoon bloodMoon = plugin.getNightManager().getBloodMoon();

        if (bloodMoon.getState() == NightMare.State.RUNNING) {
            if (bloodMoon.getAffectedWorlds().contains(entity.getWorld())) {
                applyModifiers(entity, NightMare.Type.BLOOD_MOON);
                handleBloodMoon(event, entity);
            }
        }
        HeadLessHorseMan headLessHorseMan = plugin.getNightManager().getHeadLessHorseMan();

        if (headLessHorseMan.getState() == NightMare.State.RUNNING) {
            if (headLessHorseMan.getAffectedWorlds().contains(entity.getWorld())) {
                applyModifiers(entity, NightMare.Type.HEADLESS_HORSEMAN);
                handleHeadLessHorseMan(event, entity);
            }
        }
    }
    private void applyModifiers(Entity entity, NightMare.Type type){
        String key = type.name().toLowerCase();
        double health = plugin.getConfig().getDouble(key + ".enemy_modifier.health",1);
        double damage = plugin.getConfig().getDouble(key + ".enemy_modifier.damage",1);
        double speed = plugin.getConfig().getDouble(key + ".enemy_modifier.speed",1);

        Enemy enemy = (Enemy) entity;

        double baseHealth = enemy.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        double baseDamage = enemy.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
        double baseSpeed = enemy.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();

        enemy.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(baseHealth * health);
        enemy.setHealth(baseHealth * health);
        enemy.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(baseDamage * damage);
        enemy.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(baseSpeed * speed);

    }
    private void handleBloodMoon(CreatureSpawnEvent event, Entity entity){
        Enemy enemy = (Enemy) entity;
        if (event.isCancelled()){
            return;
        }
        if (event.getSpawnReason() != SpawnReason.NATURAL){
            return;
        }

        if (entity.getType() == EntityType.ENDER_DRAGON || entity.getType() == EntityType.WITHER){
            return;
        }
        if (entity.getType() == EntityType.SPIDER){
            Entity newEntity = entity.getWorld().spawnEntity(entity.getLocation(), EntityType.WITCH);
            enemy.addPassenger(newEntity);
        }
        entity.setVisualFire(true);

    }

    private void handleHeadLessHorseMan(CreatureSpawnEvent event, Entity entity){
        Enemy enemy = (Enemy) entity;
        if (event.isCancelled()){
            return;
        }
        if (event.getSpawnReason() != SpawnReason.NATURAL){
            return;
        }
        
        if (entity.getType() == EntityType.ENDER_DRAGON || entity.getType() == EntityType.WITHER){
            return;
        }
        
    }
}
