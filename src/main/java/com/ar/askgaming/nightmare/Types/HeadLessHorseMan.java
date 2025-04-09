package com.ar.askgaming.nightmare.Types;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.ar.askgaming.nightmare.NightMare;

public class HeadLessHorseMan extends NightAbstract {

    public HeadLessHorseMan() {
        super(NightMare.Type.HEADLESS_HORSEMAN);
    }
    private WitherSkeleton witherSkeleton = null;

    private PotionEffect potion = new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 0);
    private ItemStack chest = new ItemStack(Material.NETHERITE_CHESTPLATE);
    private ItemStack legs = new ItemStack(Material.NETHERITE_LEGGINGS);
    private ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);

    public void loadConfig() {
        super.loadConfig();
        
    }

    @Override
    public void start() {
    }

    @Override
    public void end() {


    }  
    @Override
    protected void spawnBoss(List<Player> players) {
        for (Player player : players) {
            player.sendMessage(plugin.getLang().get(type.name().toLowerCase()+".spawn_boss", player));
            int y = player.getWorld().getHighestBlockYAt(player.getLocation()) + 1;
            Location loc = new Location(player.getWorld(), player.getLocation().getX(), y, player.getLocation().getZ());
            spawnEntity(loc);
        }
    }
    private void spawnEntity(Location loc){
        World world = loc.getWorld();
        Entity horse = world.spawnEntity(loc, EntityType.SKELETON_HORSE);
        Entity boss = world.spawnEntity(loc, EntityType.WITHER_SKELETON);

        applyAttributes(boss);

        SkeletonHorse horseEntity = (SkeletonHorse) horse;
        horseEntity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 1));
  
        AbstractHorseInventory horseInventory = horseEntity.getInventory();
        horseInventory.setSaddle(new ItemStack(Material.SADDLE));
        //horseInventory.setArmor(new ItemStack(Material.GOLDEN_HORSE_ARMOR));

        witherSkeleton = (WitherSkeleton) boss;

        witherSkeleton.addPotionEffect(potion);
        witherSkeleton.getEquipment().setChestplate(chest.clone());
        witherSkeleton.getEquipment().setLeggings(legs.clone());
        witherSkeleton.getEquipment().setBoots(boots.clone());

        witherSkeleton.setCustomName("Headless Horseman");
        witherSkeleton.setCustomNameVisible(true);

        horse.addPassenger(boss);
    }

    @Override
    public void run() {
        
        if (state == NightMare.State.RUNNING) {
            for (Player player : getAffectedPlayers()) {

                Location center = player.getLocation();

                double distance = Math.random() * 15; // Hasta 10 bloques de distancia
        
                double x = center.getX() + distance * (Math.random() - 0.5);
                double z = center.getZ() + distance * (Math.random() - 0.5);
                
                player.getWorld().strikeLightningEffect(center.clone().add(x, 0, z));        
                
            }
            if (coutdown > 0) {
                coutdown--;
            } else {
                endEvent();
            }
        } 
    }
    public WitherSkeleton getWitherSkeleton() {
        return witherSkeleton;
    }
}
