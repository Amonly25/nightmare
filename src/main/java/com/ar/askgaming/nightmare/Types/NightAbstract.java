package com.ar.askgaming.nightmare.Types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.ar.askgaming.nightmare.NightMare;

public abstract class NightAbstract {

    protected List<World> affectedWorlds = new ArrayList<>();
    protected final Type type;

    protected final NightMare plugin;

    public NightAbstract(Type type) {
        this.plugin = NightMare.getInstance();
        this.type = type;

        loadConfig();
    }
    
    public enum Type {
        BLOOD_MOON,
        HEADLESS_HORSEMAN,
        //HEROBRINE,
        //APOCALYPSE,
    }
    protected void loadConfig() {
        affectedWorlds.clear();
        List<String> list = plugin.getConfig().getStringList(type.name() + ".enabled_worlds");
        for (String worldName : list) {
            World world = plugin.getServer().getWorld(worldName);
            if (world != null) {
                affectedWorlds.add(world);
            }
        }
    }
    protected List<Player> getAffectedPlayers() {
        List<Player> players = new ArrayList<>();
        for (World world : affectedWorlds) {
            players.addAll(world.getPlayers());
        }
        return players;
    }

    // Método para iniciar el evento
    public abstract void startEvent();

    // Método para finalizar el evento
    public abstract void endEvent();
    
}
