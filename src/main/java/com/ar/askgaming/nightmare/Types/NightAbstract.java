package com.ar.askgaming.nightmare.Types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.ar.askgaming.nightmare.NightMare;

public abstract class NightAbstract extends BukkitRunnable{

    protected List<World> affectedWorlds = new ArrayList<>();
    protected final NightMare.Type type;
    protected NightMare.State state = NightMare.State.WAITING;
    protected BossBar bossBar;

    protected Integer duration, coutdown;

    protected final NightMare plugin;

    public NightAbstract(NightMare.Type type) {
        this.plugin = NightMare.getInstance();
        this.type = type;

        createBossBar();
        loadConfig();
        
        runTaskTimer(plugin, 20, 20);
    }
    
    protected void createBossBar() {
        bossBar = plugin.getServer().createBossBar(type.name(), BarColor.RED, BarStyle.SOLID);
        bossBar.setVisible(true);
    }
    protected void loadConfig() {
        duration = plugin.getConfig().getInt(type.name().toLowerCase() + ".duration",600);
        coutdown = duration;
        String title = plugin.getConfig().getString(type.name().toLowerCase() + ".title", type.name());
        if (bossBar != null) {
            bossBar.setTitle(title);
        }

        affectedWorlds.clear();
        List<String> list = plugin.getConfig().getStringList(type.name().toLowerCase() + ".enabled_worlds");

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
    public void startEvent(){
        if (state == NightMare.State.RUNNING) {
            return;
        }
        state = NightMare.State.RUNNING;
        coutdown = duration;
        getAffectedPlayers().forEach(player -> bossBar.addPlayer(player));

        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.sendMessage(plugin.getLang().get(type.name().toLowerCase()+".start", pl));
        }
        bossBar.setProgress(1);

        getAffectedWorlds().forEach(world -> {
            world.setTime(18000);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        });

        start();
    }

    // Método para finalizar el evento
    public void endEvent() {
        if (state == NightMare.State.WAITING) {
            return;
        }
        state = NightMare.State.WAITING;
        bossBar.removeAll();
        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.sendMessage(plugin.getLang().get(type.name().toLowerCase()+".end", pl));
        }
        getAffectedWorlds().forEach(world -> {
            world.setTime(0);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        });
        end();

    }
    protected abstract void start();
    protected abstract void end();


    public Integer getDuration() {
        return duration;
    }
    public BossBar getBossBar() {
        return bossBar;
    }
    public NightMare.Type getType() {
        return type;
    }
    public NightMare.State getState() {
        return state;
    }
    public List<World> getAffectedWorlds() {
        return affectedWorlds;
    }
    public void updateBossBar() {

        if (bossBar != null) {
            bossBar.setProgress((double) coutdown / duration);
        }
    }
    public void setCountdown(Integer countdown) {
        this.coutdown = countdown;
    }
    public Integer getCountdown() {
        return coutdown;
    }

    public boolean hasBlockAbove(Player player){
        Location loc = player.getLocation();

        if (player.getWorld().getHighestBlockAt(loc).getY() > loc.getY()){
            return true;

        }
        return false;
    }    
}
