package com.ar.askgaming.nightmare;

import org.bukkit.plugin.java.JavaPlugin;

import com.ar.askgaming.nightmare.Controllers.Commands;
import com.ar.askgaming.nightmare.Controllers.Language;
import com.ar.askgaming.nightmare.Controllers.PlaceHolders;
import com.ar.askgaming.nightmare.Listeners.CreatureSpawnListener;

public class NightMare extends JavaPlugin{
    
    private static NightMare instance;

    private Language lang;
    private NightManager nightManager;

    public void onEnable(){
        
        instance = this;

        saveDefaultConfig();

        new Commands();

        lang = new Language();
        nightManager = new NightManager();

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceHolders();
        }

        new CreatureSpawnListener();
    }
    

    public void onDisable(){
        nightManager.cancel();
        
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
    public enum Type {
        BLOOD_MOON,
        HEADLESS_HORSEMAN,
    }
    public enum State {
        WAITING,
        RUNNING,
    }

}