package com.ar.askgaming.nightmare;

import org.bukkit.plugin.java.JavaPlugin;

import com.ar.askgaming.nightmare.Controllers.Commands;
import com.ar.askgaming.nightmare.Controllers.Language;
import com.ar.askgaming.nightmare.Controllers.PlaceHolders;

public class NightMare extends JavaPlugin{
    
    private static NightMare instance;

    private Language lang;

    public void onEnable(){
        
        instance = this;

        saveDefaultConfig();

        new Commands();

        lang = new Language();

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceHolders();
        }
    }
    

    public void onDisable(){
        
    }

    public static NightMare getInstance(){
        return instance;
    }
    public Language getLang(){
        return lang;
    }

}