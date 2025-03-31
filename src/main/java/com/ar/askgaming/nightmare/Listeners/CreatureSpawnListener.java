package com.ar.askgaming.nightmare.Listeners;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import com.ar.askgaming.nightmare.NightMare;
import com.ar.askgaming.nightmare.Types.BloodMoon;
import com.ar.askgaming.nightmare.Types.HeadLessHorseMan;

public class CreatureSpawnListener implements Listener{

    private final NightMare plugin;

    public CreatureSpawnListener() {
        this.plugin = NightMare.getInstance();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Enemy)){
            return;
        }
        
        BloodMoon bloodMoon = plugin.getNightManager().getBloodMoon();

        if (bloodMoon.getState() == NightMare.State.RUNNING) {
            if (bloodMoon.getAffectedWorlds().contains(entity.getWorld())) {
                applyModifiers(entity, NightMare.Type.BLOOD_MOON);
                handleBloodMoon(event, entity);
            }
        }
        HeadLessHorseMan headLessHorseMan = plugin.getNightManager().getHeadLessHorseMan();

        if (headLessHorseMan.getState() == NightMare.State.RUNNING) {
            if (headLessHorseMan.getAffectedWorlds().contains(entity.getWorld())) {
                applyModifiers(entity, NightMare.Type.HEADLESS_HORSEMAN);
                handleHeadLessHorseMan(event, entity);
            }
        }
    }
    private void applyModifiers(Entity entity, NightMare.Type type){
        String key = type.name().toLowerCase();
        double health = plugin.getConfig().getDouble(key + ".enemy_modifier.health",1);
        double damage = plugin.getConfig().getDouble(key + ".enemy_modifier.damage",1);
        double speed = plugin.getConfig().getDouble(key + ".enemy_modifier.speed",1);

        Enemy enemy = (Enemy) entity;

        double baseHealth = enemy.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        double baseDamage = enemy.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
        double baseSpeed = enemy.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();

        enemy.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(baseHealth * health);
        enemy.setHealth(baseHealth * health);
        enemy.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(baseDamage * damage);
        enemy.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(baseSpeed * speed);

    }
    private void handleBloodMoon(CreatureSpawnEvent event, Entity entity){
        Enemy enemy = (Enemy) entity;
        if (event.isCancelled()){
            return;
        }
        if (event.getSpawnReason() != SpawnReason.NATURAL){
            return;
        }

        if (entity.getType() == EntityType.ENDER_DRAGON || entity.getType() == EntityType.WITHER){
            return;
        }

        entity.getWorld().spawnParticle(Particle.LAVA, entity.getLocation(), 30, 1, 1, 1, 0);
        switch (enemy.getType()){ 
            case SPIDER:
                Entity newEntity = entity.getWorld().spawnEntity(entity.getLocation(), EntityType.WITCH);
                enemy.addPassenger(newEntity);
                break;
            case ZOMBIE:
                enemy.getEquipment().setHelmet(makeSkull(zombies.get((int) (Math.random() * zombies.size()))));
                break;
            case SKELETON:
                enemy.getEquipment().setHelmet(makeSkull(skeletons.get((int) (Math.random() * skeletons.size()))));
                break;
            case ENDERMAN:
                enemy.getEquipment().setHelmet(makeSkull("http://textures.minecraft.net/texture/64b2c6d8400545a6ac414bae92d57a81f225e8d542c6ff0e3756727601249741"));
                break;
            case STRAY:
                enemy.getEquipment().setHelmet(makeSkull("http://textures.minecraft.net/texture/286985bba5f3af694442e465644e215a4efbfbce4707f88c268120c42720cda6"));
                break;
            case HUSK:
                enemy.getEquipment().setHelmet(makeSkull("http://textures.minecraft.net/texture/85c09612f0f265a1db2d62e8035ab9cfa5f1b949024d90494de9ca02d59f3f15"));
                break;
            case DROWNED:
                enemy.getEquipment().setHelmet(makeSkull("http://textures.minecraft.net/texture/7dc2e7585dd9a4f10797ef3581510c6c7fc9ce2aa99be5eb604c7b8e3f766b54"));
                break;
            case ZOMBIE_VILLAGER:
                enemy.getEquipment().setHelmet(makeSkull("http://textures.minecraft.net/texture/b7350fd2df430bc4b9aaecd13c51c951e5bfc9f133bad0cdaefe1922a9e47fa3"));
                break;
            case WITHER_SKELETON:
                enemy.getEquipment().setHelmet(makeSkull("http://textures.minecraft.net/texture/daa4e2294df370b9a50cb924cdda78f740b0fbaf5a687106178505c80a79addc"));
                break;
  
            default:
                break;
        }

    }

    private void handleHeadLessHorseMan(CreatureSpawnEvent event, Entity entity){
        Enemy enemy = (Enemy) entity;
        if (event.isCancelled()){
            return;
        }
        if (event.getSpawnReason() != SpawnReason.NATURAL){
            return;
        }
        
        if (entity.getType() == EntityType.ENDER_DRAGON || entity.getType() == EntityType.WITHER){
            return;
        }
        
    }
 
    private ItemStack makeSkull(String url) {

        // Crear el item de la cabeza de jugador
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

		PlayerProfile pf = plugin.getServer().createPlayerProfile("Steve");
        PlayerTextures textures = pf.getTextures();
    
        try {
            URL URL = URI.create(url).toURL();
            textures.setSkin(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        pf.setTextures(textures);

        meta.setOwnerProfile(pf);

        skull.setItemMeta(meta);
        return skull;
    }

    private List<String> skeletons = new ArrayList<>(List.of(
        "http://textures.minecraft.net/texture/84365aa8fe37d35cb9f47685549ff839e796a6098a466d0f407eaf9f7e5e819b",
        "http://textures.minecraft.net/texture/b2d22a51352ba411ec6873276b628195e88836c6975f2d4512e6167889a5f",
        "http://textures.minecraft.net/texture/2de7bbf325ad27b02f9a95b2409fb0ba065a77a4ba90ee9b91f68d8f29a679c7",
        "http://textures.minecraft.net/texture/90722a814efd47b8ecdeadfb53ab26462120a2ea6d76e24763083778598da82b",
        "http://textures.minecraft.net/texture/a1a6e5f141929c8658a58b27ae2ab896a4f5b7beb5ca31859547b6d8c6363403"

    ));

    private List<String> zombies = new ArrayList<>(List.of(
        "http://textures.minecraft.net/texture/d2761381a96499d80e2d9c93818d2bf407e15a3ff1fc0b2af17babae8f200b2f",
        "http://textures.minecraft.net/texture/48a9680d4e9f41917d90f0aacb68f46f49283e2df1cb0fe3a70ee8ef5263fea9",
        "http://textures.minecraft.net/texture/a5d1462d4478058b6a01b529611852c6a2187038aa064566726d5b72583047d7",
        "http://textures.minecraft.net/texture/c3fab76f718092d64b7b00ddb72931c0b05e8cbb687106c3c6f3560f933f9930",
        "http://textures.minecraft.net/texture/b2a1fd2d7cfbdd2a8d9b3f81f7312705b89a44d38b841011a7e79ea6b9bc8584",
        "http://textures.minecraft.net/texture/faac2230159a803d28cfde6662eaf379da89a8a073c7be20c6e7e408dd8861d1"

    ));
    
}
