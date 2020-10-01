package com.dazzhub.skywars.Utils;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Tools {

    public static float randomRange(float min, float max) {
        return min + (float)Math.random() * (max - min);
    }

    public static boolean spawnEntities(Location location, EntityType obj, ItemStack stack, int amount, int radius, boolean isOnFire, boolean isLightning) {
        Random rdm = new Random();
        if (isLightning) {
            Location newLoc = location.clone();
            newLoc.setX(location.getX() + rdm.nextInt(radius * 2) - radius);
            newLoc.setZ(location.getZ() + rdm.nextInt(radius * 2) - radius);
            World world = location.getWorld();
            newLoc = world.getHighestBlockAt(newLoc.clone()).getLocation();
            world.createExplosion(newLoc, 9);
            world.strikeLightning(newLoc);
            return true;
        }
        try {
            for (int i = 0; i < amount; ++i) {
                Location newLoc = location.clone();
                newLoc.setX(location.getX() + rdm.nextInt(radius * 2) - radius);
                newLoc.setY(location.getY() + rdm.nextInt(15));
                newLoc.setZ(location.getZ() + rdm.nextInt(radius * 2) - radius);
                if (obj != null) {
                    Entity creature = location.getWorld().spawn(newLoc, obj.getEntityClass());
                    if (creature instanceof Fireball) {
                        ((Fireball) creature).setDirection(new Vector(0, -1, 0));
                    }
                    if (creature instanceof TNTPrimed) {
                        ((TNTPrimed) creature).setFuseTicks(50);
                    }
                    if (isOnFire) {
                        creature.setFireTicks(1000 + (int) rdm.nextFloat() * 300);
                    }
                } else {
                    if (stack != null) {
                        location.getWorld().dropItem(newLoc, stack);
                    }
                }
            }
        } catch (Exception e) {
            Console.warning("This entity or world is invalid");
            return false;
        }
        return true;
    }
}
