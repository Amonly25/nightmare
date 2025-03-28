package com.ar.askgaming.nightmare.Types;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import com.ar.askgaming.nightmare.NightMare;

public class BloodMoon extends NightAbstract {

    public BloodMoon() {
        super(NightMare.Type.BLOOD_MOON);

        
        runTaskTimer(plugin, 20, 20);
    }

    @Override
    public void start() {


    }

    @Override
    public void end() {

        
    }

    @Override
    public void run() {
        updateBossBar();

        if (state == NightMare.State.RUNNING) {
            for (Player player : getAffectedPlayers()) {
                Location center = player.getLocation();

                for (int i = 0; i < 20; i++) { // Generar múltiples gotas en cada ciclo
                    double angle = Math.random() * 360;
                    double distance = Math.random() * 15; // Hasta 10 bloques de distancia
            
                    double x = center.getX() + Math.cos(Math.toRadians(angle)) * distance + (Math.random() - 0.5);
                    double z = center.getZ() + Math.sin(Math.toRadians(angle)) * distance + (Math.random() - 0.5);
                    
                    // Variación de altura más natural
                    double baseHeight = center.getY() + 5; // Altura base
                    double variation = Math.random() * (Math.random() > 0.5 ? 15 : 2); // Diferentes alturas para cada gota
                    double y = baseHeight + variation;
            
                    Location bloodLocation = new Location(player.getWorld(), x, y, z);
            
                    // Efecto de lluvia de sangre con LANDING_LAVA
                    player.getWorld().spawnParticle(Particle.LANDING_LAVA, bloodLocation, 3, 0, 0, 0, 0); // Reducido a 3 partículas para mejor dispersión           
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
