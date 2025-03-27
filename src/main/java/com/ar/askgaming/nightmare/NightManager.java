package com.ar.askgaming.nightmare;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.ar.askgaming.nightmare.Types.BloodMoon;
import com.ar.askgaming.nightmare.Types.HeadLessHorseMan;

public class NightManager extends BukkitRunnable{

    private final NightMare plugin;
    private final BloodMoon bloodMoon;
    private final HeadLessHorseMan headLessHorseMan;

    public NightManager() {
        this.plugin = NightMare.getInstance();

        bloodMoon = new BloodMoon();
        headLessHorseMan = new HeadLessHorseMan();
        
        runTaskTimer(plugin, 20*60, 20*60);
    }

    @Override
    public void run() {

        checkTimeForScheduler();

        if (bloodMoon.getState() == NightMare.State.RUNNING) {
            bloodMoon.updateBossBar();
            int countdown = bloodMoon.getCountdown();
            if (countdown > 0) {
                bloodMoon.setCountdown(countdown - 1);
            } else {
                bloodMoon.endEvent();
            }
        }
        if (headLessHorseMan.getState() == NightMare.State.RUNNING) {
            headLessHorseMan.updateBossBar();
            int countdown = headLessHorseMan.getCountdown();
            if (countdown > 0) {
                headLessHorseMan.setCountdown(countdown - 1);
            } else {
                headLessHorseMan.endEvent();
            }
        }
     
    }
    //#region checktime
    private void checkTimeForScheduler() {

        ConfigurationSection scheduler = plugin.getConfig().getConfigurationSection("scheduler");
        if (scheduler == null) {
            plugin.getLogger().severe("No scheduler section found in config");
            return;
        }

        String currentDay = java.time.LocalDate.now().getDayOfWeek().name().toLowerCase();
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        ConfigurationSection daySection = scheduler.getConfigurationSection(currentDay);
        if (daySection != null) {
            Set<String> times = daySection.getKeys(false);
            for (String time : times) {
                if (time.equals(currentTime)) {
                    String type = daySection.getString(time);
                    if (type == null || type.isEmpty()) {
                        continue;
                    }
                    if (type.equalsIgnoreCase("BLOOD_MOON")) {
                        bloodMoon.startEvent();
                    } else if (type.equalsIgnoreCase("HEADLESS_HORSEMAN")) {
                        headLessHorseMan.startEvent();
                    }
                }
            }
        }
    }
    //#region Getters

    public BloodMoon getBloodMoon() {
        return bloodMoon;
    }

    public HeadLessHorseMan getHeadLessHorseMan() {
        return headLessHorseMan;
    }
    //#region Utils
    public void sendCommandIfExist(List<String> list, Player player) {

        if (list == null || list.isEmpty()) {
            return;
        }
        for (String command : list) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command.replace("%player%", player.getName()));
        }
    }
    public void addPotionEffect(List<String> list, Player player) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (String effect : list) {
            String[] split = effect.split(":");
            try {
                int duration = Integer.parseInt(split[1]);
                int amplifier = Integer.parseInt(split[2]);
                PotionEffectType type = Registry.EFFECT.match(split[0].toUpperCase()); // Check if the effect is in the
                player.addPotionEffect(new PotionEffect(type, duration, amplifier));
            } catch (Exception e) {
                plugin.getLogger().warning("Wrong potion effect format: " + effect);
                e.printStackTrace();
            }
        }
    }
    public void playSoundIfExist(List<String> list, Player player) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (String s : list) {
            try {
                player.playSound(player.getLocation(), Sound.valueOf(s.toUpperCase()), 3, 1);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid sound: " + s);
                return;
            }
        }
    }
    public void addDrops(List<String> list, Location loc) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (String drop : list) {
            String[] split = drop.split(":");
            try {
                int amount = Integer.parseInt(split[1]);
                ItemStack item = new ItemStack(Material.valueOf(split[0]), amount);
                loc.getWorld().dropItem(loc, item);
            } catch (Exception e) {
                plugin.getLogger().warning("Wrong drop format: " + drop);
                e.printStackTrace();
            }
        }
    }
    private final Map<UUID, BukkitTask> bleedingPlayers = new HashMap<>();

    public void startBleeding(Player player, int duration, double damagePerTick) {
        if (bleedingPlayers.containsKey(player.getUniqueId())) return; // Si ya está sangrando, no lo agregamos

        BukkitTask task = new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (!player.isOnline() || player.isDead()) {
                    stopBleeding(player);
                    return;
                }

                // Efecto visual de sangre (partículas)
                player.getWorld().spawnParticle(Particle.DUST, player.getLocation().add(0, 1, 0), 10,
                        0.3, 0.2, 0.3, new Particle.DustOptions(Color.RED, 1.5f));

                // Daño por sangrado
                player.damage(damagePerTick);
                
                ticks += 20;
                if (ticks >= duration) {
                    stopBleeding(player);
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Se ejecuta cada 1 segundo (20 ticks)

        bleedingPlayers.put(player.getUniqueId(), task);
    }

    public void stopBleeding(Player player) {
        if (bleedingPlayers.containsKey(player.getUniqueId())) {
            bleedingPlayers.get(player.getUniqueId()).cancel();
            bleedingPlayers.remove(player.getUniqueId());
        }
    }
}
