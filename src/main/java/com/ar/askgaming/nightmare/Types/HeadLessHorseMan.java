package com.ar.askgaming.nightmare.Types;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.ar.askgaming.nightmare.NightMare;

public class HeadLessHorseMan extends NightAbstract {

    public HeadLessHorseMan() {
        super(NightMare.Type.HEADLESS_HORSEMAN);
    }

    @Override
    public void start() {

        spawnBoss();
    }

    @Override
    public void end() {


    }  

    private void spawnBoss(){

        PotionEffect potion = new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 0);

        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

        getAffectedPlayers().forEach(player -> {
            Location loc = player.getLocation();
            World world = loc.getWorld();

            Entity horse = world.spawnEntity(loc, EntityType.SKELETON_HORSE);

            Entity boss = world.spawnEntity(loc, EntityType.WITHER_SKELETON);
            boss.addPassenger(horse);
            Enemy enemy = (Enemy) boss;

            enemy.addPotionEffect(potion);

            enemy.getEquipment().setChestplate(chest.clone());
            enemy.getEquipment().setLeggings(legs.clone());
            enemy.getEquipment().setBoots(boots.clone());

            enemy.setCustomName("Headless Horseman");
            enemy.setCustomNameVisible(true);
        });
    }
}
