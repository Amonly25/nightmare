package com.ar.askgaming.nightmare.Listeners;

import java.util.List;

import org.bukkit.Particle;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.ar.askgaming.nightmare.NightMare;
import com.ar.askgaming.nightmare.Types.BloodMoon;
import com.ar.askgaming.nightmare.Types.HeadLessHorseMan;

public class EntityDamageByEntityListener implements Listener{

    private final NightMare plugin;

    public EntityDamageByEntityListener() {
        this.plugin = NightMare.getInstance();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damaged = event.getEntity();
        Entity damager = event.getDamager();
        if (damaged instanceof Player && damager instanceof Enemy) {

            Player player = (Player) damaged;
            Enemy enemy = (Enemy) damager;

            BloodMoon bloodMoon = plugin.getNightManager().getBloodMoon();

            if (bloodMoon.getState() == NightMare.State.RUNNING) {
                if (bloodMoon.getAffectedWorlds().contains(player.getWorld())) {
                    handleNightDamage(player, enemy, NightMare.Type.BLOOD_MOON);
                }
            }
            HeadLessHorseMan headLessHorseMan = plugin.getNightManager().getHeadLessHorseMan();

            if (headLessHorseMan.getState() == NightMare.State.RUNNING) {
                if (headLessHorseMan.getAffectedWorlds().contains(player.getWorld())) {
                    handleNightDamage(player, enemy, NightMare.Type.HEADLESS_HORSEMAN);
                }
            }
        }
    }
    private void handleNightDamage(Player player, Enemy enemy, NightMare.Type type){
        String key = type.name().toLowerCase();
        List<String> effects = plugin.getConfig().getStringList(key + ".on_enemy_attack.effects");
        List<String> sounds = plugin.getConfig().getStringList(key + ".on_enemy_attack.sounds");

        boolean bleeding = plugin.getConfig().getBoolean(key + ".on_enemy_attack.bleeding", false);
        if (bleeding) {
            plugin.getNightManager().startBleeding(player, 2, 1);
        }

        if (enemy.equals(plugin.getNightManager().getHeadLessHorseMan().getWitherSkeleton())) {
            enemy.getWorld().spawnParticle(Particle.LAVA, player.getLocation(), 30, 1, 1, 1, 0);

        }
        
        plugin.getNightManager().addPotionEffect(effects, player);
        plugin.getNightManager().playSoundIfExist(sounds, player);
    }
}
