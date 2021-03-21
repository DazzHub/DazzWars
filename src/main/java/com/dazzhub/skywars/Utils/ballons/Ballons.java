package com.dazzhub.skywars.Utils.ballons;

import com.cryptomorin.xseries.XMaterial;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.inventory.Icon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Ballons {

    private Monster giant;

    private ArmorStand string1;
    private Bat string2;
    private ArmorStand string3;

    public void spawn(){
        World world = Bukkit.getWorld("world");
        Location loc = Main.getPlugin().getLobbyManager().getLobby();

        /////////////////////////////////////////////////////////////////
        this.giant = (Giant) world.spawnEntity(loc, EntityType.GIANT);

        EntityEquipment equipment = giant.getEquipment();
        this.giant.setCustomNameVisible(false);

        equipment.setItemInHand(new Icon(XMaterial.PLAYER_HEAD,1, (short) 3).setSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdjMTRlYjg3ZGM2NDQ0YWU2MjVmMTIyY2YzYWU5NmZjOGEyODZhYmI2OWRjYzc4ZWU5NWNkNDQzMjMyYTA1YyJ9fX0=").build());
        giant.setLeashHolder(null);
        this.giant.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000,1));
        ///////////////////////////////////////////////////////////////////
        this.string1 = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
        this.string1.setGravity(false);
        this.string1.setVisible(false);

        this.string2 = (Bat) world.spawnEntity(loc.add(4,7,0), EntityType.BAT);
        this.string2.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000,1));

        this.string3 = (ArmorStand) world.spawnEntity(loc.add(4,7,0), EntityType.ARMOR_STAND);
        this.string3.setVisible(false);
        this.string3.setGravity(false);

        this.string3.setPassenger(string2);

        string2.setLeashHolder(string1);
        ///////////////////////////////////////////////////////////////////
    }

    public void delete(){
        if (giant != null) {
            this.giant.remove();
            this.string1.remove();
            this.string2.remove();
        }
    }

}
