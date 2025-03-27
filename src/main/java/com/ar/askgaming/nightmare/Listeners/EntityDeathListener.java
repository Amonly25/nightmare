package com.ar.askgaming.nightmare.Listeners;

import java.util.List;

import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.ar.askgaming.nightmare.NightMare;
import com.ar.askgaming.nightmare.Types.BloodMoon;
import com.ar.askgaming.nightmare.Types.HeadLessHorseMan;

public class EntityDeathListener implements Listener{

    private final NightMare plugin;

    public EntityDeathListener() {
        this.plugin = NightMare.getInstance();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Enemy) {
            Enemy enemy = (Enemy) entity;

            BloodMoon bloodMoon = plugin.getNightManager().getBloodMoon();

            if (bloodMoon.getState() == NightMare.State.RUNNING) {
                if (bloodMoon.getAffectedWorlds().contains(enemy.getWorld())) {
                    List<String> drops = plugin.getConfig().getStringList("blood_moon.on_enemy_death.additional_drops");
                    plugin.getNightManager().addDrops(drops, enemy.getLocation());
                }
            }
            HeadLessHorseMan headLessHorseMan = plugin.getNightManager().getHeadLessHorseMan();

            if (headLessHorseMan.getState() == NightMare.State.RUNNING) {
                if (headLessHorseMan.getAffectedWorlds().contains(enemy.getWorld())) {
                    List<String> drops = plugin.getConfig().getStringList("headless_horseman.on_enemy_death.additional_drops");
                    plugin.getNightManager().addDrops(drops, enemy.getLocation());
                }
            }
        }
    }
}
