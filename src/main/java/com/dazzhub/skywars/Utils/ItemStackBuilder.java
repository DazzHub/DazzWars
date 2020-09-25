package com.dazzhub.skywars.Utils;

import com.dazzhub.skywars.Utils.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class ItemStackBuilder {
    private ItemStack is;

    public ItemStackBuilder(XMaterial m) {
        this(m, 1);
    }

    public ItemStackBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemStackBuilder(XMaterial m, int amount) {
        is = new ItemStack(m.parseItem().getType(), amount);
    }

    public ItemStackBuilder clone() {
        return new ItemStackBuilder(is);
    }

    public ItemStackBuilder setDurability(short dur) {
        is.setDurability(dur);
        return this;
    }

    public ItemStackBuilder setType(XMaterial m) {
        is.setType(m.parseItem().getType());
        return this;
    }

    public ItemStackBuilder addBookEnchantment(Enchantment enchantment, int level) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta)is.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        is.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder hideEnch() {
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(im);
        return this;
    }

    public ItemStackBuilder setName(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&',name));
        is.setItemMeta(im);
        return this;
    }

    public ItemStackBuilder setDisplayName(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&',name));
        is.setItemMeta(im);
        return this;
    }

    public List<String> getLore() {
        return is.getItemMeta().getLore();
    }

    public ItemStackBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemStackBuilder removeEnchantment(Enchantment ench) {
        is.removeEnchantment(ench);
        return this;
    }

    public ItemStackBuilder setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(owner);
            is.setItemMeta(im);
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    public ItemStackBuilder setSkullTexture(String url) {
        if(url == null)
            return this;
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            GameProfile localGameProfile = new GameProfile(UUID.randomUUID(), null);
            byte[] arrayOfByte = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", new Object[] { url }).getBytes());
            localGameProfile.getProperties().put("textures", new Property("textures", new String(arrayOfByte)));
            Field localField = null;
            try
            {
                localField = im.getClass().getDeclaredField("profile");
                localField.setAccessible(true);
                localField.set(im, localGameProfile);
            }
            catch (NoSuchFieldException|IllegalArgumentException|IllegalAccessException localNoSuchFieldException)
            {
                System.out.println("error: " + localNoSuchFieldException.getMessage());
            }
            is.setItemMeta(im);
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    public ItemStackBuilder addEnchant(Enchantment ench, int level) {
        if(ench != null) {
            ItemMeta im = is.getItemMeta();
            im.addEnchant(ench, level, true);
            is.setItemMeta(im);
        }
        return this;
    }

    public ItemStackBuilder setLore(String... lore) {
        ItemMeta im = is.getItemMeta();
        ArrayList<String> fullLore = new ArrayList<>();
        for(String s : lore) {
            fullLore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        im.setLore(fullLore);
        is.setItemMeta(im);
        return this;
    }

    public ItemStackBuilder setLore(List<String> lore) {
        ItemMeta im = is.getItemMeta();
        ArrayList<String> fullLore = new ArrayList<>();

        for(String s : lore) {
            fullLore.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        im.setLore(fullLore);
        is.setItemMeta(im);
        return this;
    }


    public ItemStackBuilder setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
            im.setColor(color);
            is.setItemMeta(im);
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    public ItemStack toItemStack() {
        return is;
    }

    public ItemStack build() {
        return is;
    }
}

