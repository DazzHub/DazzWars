package com.dazzhub.skywars.Utils.inventory;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

public class listEnchants
{
    public static String getBukkitVersion() {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf(46) + 1);
    }

    public static Enchantment getEnchantmentFromString(String input) {
        if (input == null || input.length() == 0) {
            return null;
        }
        if (isValidInteger(input)) {
            return Enchantment.getByName(String.valueOf(Integer.parseInt(input)));
        }
        String formattedInput = input.toLowerCase().replace("_", "").replace(" ", "");
        if (formattedInput.equalsIgnoreCase("protection")) {
            return Enchantment.PROTECTION_ENVIRONMENTAL;
        }
        if (formattedInput.equalsIgnoreCase("fireprotection")) {
            return Enchantment.PROTECTION_FIRE;
        }
        if (formattedInput.equalsIgnoreCase("featherfalling")) {
            return Enchantment.PROTECTION_FALL;
        }
        if (formattedInput.equalsIgnoreCase("blastprotection")) {
            return Enchantment.PROTECTION_EXPLOSIONS;
        }
        if (formattedInput.equalsIgnoreCase("projectileprotection")) {
            return Enchantment.PROTECTION_PROJECTILE;
        }
        if (formattedInput.equalsIgnoreCase("respiration")) {
            return Enchantment.OXYGEN;
        }
        if (formattedInput.equalsIgnoreCase("aquaaffinity")) {
            return Enchantment.WATER_WORKER;
        }
        if (formattedInput.equalsIgnoreCase("thorns")) {
            return Enchantment.THORNS;
        }
        if (formattedInput.equalsIgnoreCase("sharpness")) {
            return Enchantment.DAMAGE_ALL;
        }
        if (formattedInput.equalsIgnoreCase("smite")) {
            return Enchantment.DAMAGE_UNDEAD;
        }
        if (formattedInput.equalsIgnoreCase("baneofarthropods")) {
            return Enchantment.DAMAGE_ARTHROPODS;
        }
        if (formattedInput.equalsIgnoreCase("knockback")) {
            return Enchantment.KNOCKBACK;
        }
        if (formattedInput.equalsIgnoreCase("fireaspect")) {
            return Enchantment.FIRE_ASPECT;
        }
        if (formattedInput.equalsIgnoreCase("looting")) {
            return Enchantment.LOOT_BONUS_MOBS;
        }
        if (formattedInput.equalsIgnoreCase("efficiency")) {
            return Enchantment.DIG_SPEED;
        }
        if (formattedInput.equalsIgnoreCase("silktouch")) {
            return Enchantment.SILK_TOUCH;
        }
        if (formattedInput.equalsIgnoreCase("unbreaking")) {
            return Enchantment.DURABILITY;
        }
        if (formattedInput.equalsIgnoreCase("fortune")) {
            return Enchantment.LOOT_BONUS_BLOCKS;
        }
        if (formattedInput.equalsIgnoreCase("power")) {
            return Enchantment.ARROW_DAMAGE;
        }
        if (formattedInput.equalsIgnoreCase("punch")) {
            return Enchantment.ARROW_KNOCKBACK;
        }
        if (formattedInput.equalsIgnoreCase("flame")) {
            return Enchantment.ARROW_FIRE;
        }
        if (formattedInput.equalsIgnoreCase("infinity")) {
            return Enchantment.ARROW_INFINITE;
        }
        return Enchantment.getByName(input.toUpperCase().replace(" ", "_"));
    }

    public static boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }
}
