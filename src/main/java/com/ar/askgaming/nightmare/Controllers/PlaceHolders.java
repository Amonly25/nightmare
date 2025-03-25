package com.ar.askgaming.nightmare.Controllers;

import org.bukkit.entity.Player;

import com.ar.askgaming.nightmare.NightMare;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceHolders extends PlaceholderExpansion {

    private final NightMare plugin;

    public PlaceHolders() {
        this.plugin = NightMare.getInstance();

        register();
    }

    @Override
    public String getIdentifier() {
        return "nightmare";
    }

    @Override
    public String getAuthor() {
        return "AskGaming";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null) {
            return "";
        }

        switch (identifier) {

            default:
                return "Invalid placeholder";
        }
    }
}
