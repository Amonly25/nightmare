package com.ar.askgaming.nightmare;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

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
}
