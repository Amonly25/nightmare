package com.ar.askgaming.nightmare.Types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.ar.askgaming.nightmare.NightMare;

public abstract class NightAbstract extends BukkitRunnable{

    protected List<World> affectedWorlds = new ArrayList<>();
    protected final NightMare.Type type;
    protected NightMare.State state = NightMare.State.WAITING;
    protected BossBar bossBar;

    protected Integer duration, coutdown, damageMutiplier, SpeedMutiplier;
    protected Double bossHealth;

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
        bossHealth = plugin.getConfig().getDouble(type.name().toLowerCase() + ".boss_health", 512);
        damageMutiplier = plugin.getConfig().getInt(type.name().toLowerCase() + ".damage_multiplier", 1);
        SpeedMutiplier = plugin.getConfig().getInt(type.name().toLowerCase() + ".speed_multiplier", 1);

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
        spawnBoss(getAffectedPlayers());
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
    public void scanningToSpawnBoss() {
        int amount = Bukkit.getOnlinePlayers().size() % 10 + 1;
        int spawned = 0;
        List<Player> players = getAffectedPlayers();
        List<Player> toSpawn = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (spawned >= amount) {
                break;
            }
            spawned++;
            toSpawn.add(player);
            i--;
        }
        if (toSpawn.size() > 0) {
            spawnBoss(toSpawn);
            for (Player pl : Bukkit.getOnlinePlayers()) {
                pl.sendMessage(plugin.getLang().get(type.name().toLowerCase()+".spawn", pl).replace("{amount}", String.valueOf(spawned)));
            }
        }
    }
    protected void applyAttributes(Entity boss) {
        if (boss instanceof LivingEntity) {
            LivingEntity livingBoss = (LivingEntity) boss;
            livingBoss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(bossHealth);
            double damageBase = livingBoss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
            livingBoss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damageBase * damageMutiplier);
            livingBoss.setHealth(bossHealth);
            double speedBase = livingBoss.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
            livingBoss.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speedBase * SpeedMutiplier);
        }
    }

    protected abstract void start();
    protected abstract void end();
    protected abstract void spawnBoss(List<Player> players);

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
