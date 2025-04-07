package com.ar.askgaming.nightmare.Types;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Witch;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.ar.askgaming.nightmare.NightMare;

public class BloodMoon extends NightAbstract {

    public BloodMoon() {
        super(NightMare.Type.BLOOD_MOON);
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

  
    }
    @Override
    protected void spawnBoss(List<Player> players) {
        for (Player player : players) {
            player.sendMessage(plugin.getLang().get(type.name().toLowerCase()+".spawn_boss", player));
            int y = player.getWorld().getHighestBlockYAt(player.getLocation()) + 1;
            Location loc = player.getLocation().clone().add(0, y, 0);
            spawnEntity(loc);
        }
    }
   private void spawnEntity(Location loc) {
        LivingEntity previous = null;

        for (int i = 0; i < 5; i++) {
            // Spawnea una araña
            Spider spider = (Spider) loc.getWorld().spawnEntity(loc, EntityType.SPIDER);

            // Spawnea una bruja
            Witch witch = (Witch) loc.getWorld().spawnEntity(loc, EntityType.WITCH);

            // Hace que la bruja monte la araña
            spider.addPassenger(witch);

            // Si hay una entidad anterior (pila), la nueva araña la monta
            if (previous != null) {
                spider.addPassenger(previous);
            }

            // Actualiza la entidad anterior para la siguiente iteración
            previous = spider;
        }
    }


    @Override
    public void run() {
        updateBossBar();

        if (state == NightMare.State.RUNNING) {
            for (Player player : getAffectedPlayers()) {

                if (hasBlockAbove(player)) continue;

                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 30, 0, false, false));

                Location center = player.getLocation();

                for (int i = 0; i < 30; i++) { // Generar múltiples gotas en cada ciclo
                    double angle = Math.random() * 360;
                    double distance = Math.random() * 15; // Hasta 10 bloques de distancia
            
                    double x = center.getX() + Math.cos(Math.toRadians(angle)) * distance + (Math.random() - 0.5);
                    double z = center.getZ() + Math.sin(Math.toRadians(angle)) * distance + (Math.random() - 0.5);
                    
                    // Variación de altura más natural
                    double baseHeight = center.getY() + 10; // Altura base
                    double variation = Math.random() * (Math.random() > 0.5 ? 20 : 2); // Diferentes alturas para cada gota
                    double y = baseHeight + variation;
            
                    Location bloodLocation = new Location(player.getWorld(), x, y, z);
            
                    // Efecto de lluvia de sangre con LANDING_LAVA
                    player.getWorld().spawnParticle(Particle.FALLING_LAVA, bloodLocation, 5, 0, 0, 0, 1); // Reducido a 3 partículas para mejor dispersión           
                }
            }
            if (coutdown > 0) {
                coutdown--;
            } else {
                endEvent();
            }
        }
    }

}
