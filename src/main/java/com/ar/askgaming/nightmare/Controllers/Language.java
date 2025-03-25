package com.ar.askgaming.nightmare.Controllers;

import java.io.File;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.ar.askgaming.nightmare.NightMare;

public class Language {

    private File defaultLang;
    private final HashMap<String, HashMap<String, String>> cache = new HashMap<>();
    
    private final NightMare plugin;

    public Language(){
        this.plugin = NightMare.getInstance();

        createFile("en");    
    }

    private void createFile(String locale) {
        defaultLang = new File(plugin.getDataFolder() + "/lang/" + locale + ".yml");
        if (!defaultLang.exists()) {
            plugin.saveResource("lang/" + locale + ".yml", false);
        }
    }

    public void clearCache() {
        cache.clear();
    }

    public String get(String path, Player p) {
        String locale = (p == null) ? "en" : p.getLocale().split("_")[0];
    
        File file = new File(plugin.getDataFolder() + "/lang/" + locale + ".yml");
        if (!file.exists()) {
            locale = "en";
            file = defaultLang;
        }
    
        if (cache.containsKey(locale) && cache.get(locale).containsKey(path)) {
            return ChatColor.translateAlternateColorCodes('&', cache.get(locale).get(path));
        }
    
        String required = loadMessage(file, path);
    
        if (required.startsWith("Error:")) {
            required = loadMessage(defaultLang, path);
        }
    
        cache.computeIfAbsent(locale, k -> new HashMap<>()).put(path, required);
        return ChatColor.translateAlternateColorCodes('&', required);
    }
    
    private String loadMessage(File file, String path) {
        FileConfiguration langFile = YamlConfiguration.loadConfiguration(file);
    
        if (langFile.isList(path)) {
            return String.join("\n", langFile.getStringList(path));
        }
    
        return langFile.getString(path, "Error: Invalid lang path: " + path);  
    } 
}
