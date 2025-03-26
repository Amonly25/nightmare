package com.ar.askgaming.nightmare.Listeners;

import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.ar.askgaming.nightmare.NightMare;

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
        if (plugin.getNightManager().getBloodMoon().getState() == NightMare.State.RUNNING) {
            handleBloodMoon(entity);
        }
        if (plugin.getNightManager().getHeadLessHorseMan().getState() == NightMare.State.RUNNING) {
            handleHeadLessHorseMan(entity);
        }

    }
    private void handleBloodMoon(Entity entity){
        Enemy enemy = (Enemy) entity;

    }

    private void handleHeadLessHorseMan(Entity entity){
        Enemy enemy = (Enemy) entity;

    }
}
