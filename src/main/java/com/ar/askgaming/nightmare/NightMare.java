package com.ar.askgaming.nightmare;

import org.bukkit.plugin.java.JavaPlugin;

import com.ar.askgaming.nightmare.Controllers.Commands;
import com.ar.askgaming.nightmare.Controllers.Language;
import com.ar.askgaming.nightmare.Controllers.PlaceHolders;
import com.ar.askgaming.nightmare.Listeners.CreatureSpawnListener;
import com.ar.askgaming.nightmare.Listeners.EntityDamageByEntityListener;
import com.ar.askgaming.nightmare.Listeners.EntityDeathListener;
import com.ar.askgaming.universalnotifier.UniversalNotifier;

public class NightMare extends JavaPlugin{
    
    private static NightMare instance;

    private Language lang;
    private NightManager nightManager;
    private UniversalNotifier universalNotifier;

    public void onEnable(){
        
        instance = this;

        saveDefaultConfig();

        new Commands();

        lang = new Language();
        nightManager = new NightManager();

        if (getServer().getPluginManager().getPlugin("UniversalNotifier") != null) {
            universalNotifier = UniversalNotifier.getInstance();
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceHolders();
        }

        new CreatureSpawnListener();
        new EntityDeathListener();
        new EntityDamageByEntityListener();
    }
    

    public void onDisable(){
        nightManager.cancel();
        nightManager.getBloodMoon().endEvent();
        nightManager.getHeadLessHorseMan().endEvent();
        
    }

    public static NightMare getInstance(){
        return instance;
    }
    public NightManager getNightManager(){
        return nightManager;
    }
    public Language getLang(){
        return lang;
    }
    public UniversalNotifier getUniversalNotifier() {
        return universalNotifier;
    }
    public enum Type {
        BLOOD_MOON,
        HEADLESS_HORSEMAN,
    }
    public enum State {
        WAITING,
        RUNNING,
    }

}