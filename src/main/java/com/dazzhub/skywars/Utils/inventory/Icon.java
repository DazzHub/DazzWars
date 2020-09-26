package com.dazzhub.skywars.Utils.inventory;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class Icon {

    private ItemStack item;
    private ItemMeta im;
    private String skullOwner;

    private String permissionView;
    private ItemStack permissionViewItem;

    /* KIT */
    private int priceKit;
    private String kitName;
    private List<String> lorePurchasedKit;
    private List<String> loreSelectedKit;

    /* CAGE */
    private int priceCage;
    private String CageName;
    private List<String> lorePurchasedCage;
    private List<String> loreSelectedCage;

    public Icon(XMaterial m, int amount, short dataValue){
        item = new ItemStack(m.parseItem().getType(), amount != 0 ? amount : 1, dataValue);
        im = item.getItemMeta();

        permissionViewItem = null;

        priceKit = 0;
        kitName = "";
        lorePurchasedKit = null;
        loreSelectedKit = null;

        //////////////////////

        priceCage = 0;
        CageName = "";
        lorePurchasedCage = null;
        loreSelectedCage = null;
    }

    public Icon addDamage(short damage) {
        if (damage == 0) return this;

        short total = (short) (item.getType().getMaxDurability() - damage);
        item.setDurability(total);
        return this;
    }

    public Icon setName(String name){
        im.setDisplayName(c(name));

        item.setItemMeta(im);
        return this;
    }

    public Icon setLore(List<String> lore){
        ArrayList<String> fullLore = new ArrayList<>();

        for(String s : lore) {
            fullLore.add(c(s));
        }

        im.setLore(fullLore);
        item.setItemMeta(im);
        return this;
    }

    public Icon setKit(Integer price, String kitName, List<String> lorePurchased, List<String> loreSelected){
        this.priceKit = price;
        this.lorePurchasedKit = lorePurchased;
        this.loreSelectedKit = loreSelected;
        this.kitName = kitName;
        return this;
    }

    public Icon setCage(int price, String name, List<String> lorePurchased, List<String> loreSelected) {
        this.priceCage = price;
        this.lorePurchasedCage = lorePurchased;
        this.loreSelectedCage = loreSelected;
        this.CageName = name;
        return this;
    }

    public Icon setSkull(String skullOwner){
        this.skullOwner = skullOwner;
        return this;
    }

    public Icon addEnchantment(Map<Enchantment, Integer> enchantments) {
        enchantments.forEach((key, value) -> item.addUnsafeEnchantment(key, value));
        return this;
    }

    public Icon addPermissionView(String permissionView, Material material, short type, List<String> lore) {
        ItemStack itemPerms = new ItemStack(XMaterial.matchXMaterial(material).parseMaterial(), 1, type);
        ItemMeta meta = itemPerms.getItemMeta();

        meta.setDisplayName(im.getDisplayName());
        meta.setLore(lore);
        itemPerms.setItemMeta(meta);

        this.permissionViewItem = itemPerms;
        this.permissionView = permissionView;
        return this;
    }

    private void replaceName(Player p) {
        im.setDisplayName(c(im.getDisplayName())
                .replaceAll("%player%", p.getName())
                .replaceAll("%name%", kitName)
                .replaceAll("%cage%", CageName)
                .replaceAll("%price%", String.valueOf(priceCage)));

        item.setItemMeta(im);
    }

    private void replaceLore(Player p) {
        List<String> list = new ArrayList<>();

        for (String s : im.getLore()) {
            list.add(c(s)
                    .replaceAll("%player%", p.getName())
                    .replaceAll("%cost%", String.valueOf(priceKit))
                    .replaceAll("%cage%", CageName)
                    .replaceAll("%price%", String.valueOf(priceCage))
            );
        }

        im.setLore(list);
        item.setItemMeta(im);
    }

    private void replaceLorePurchasedKit(Player p) {
        List<String> list = new ArrayList<>();
        for (String s : lorePurchasedKit) {
            String replaceAll = c(s)
                    .replaceAll("%player%", p.getName())
                    .replaceAll("%name%", kitName)
                    .replaceAll("%cost%", String.valueOf(priceKit));
            list.add(replaceAll);
        }

        im.setLore(list);
        item.setItemMeta(im);
    }

    private void replaceLoreSelectedKit(Player p) {
        List<String> list = loreSelectedKit.stream().map(s -> c(s)
                .replaceAll("%player%", p.getName())
                .replaceAll("%name%", kitName)
                .replaceAll("%cost%", String.valueOf(priceKit)))
                .collect(Collectors.toList());

        im.setLore(list);
        item.setItemMeta(im);
    }

    private void replaceLorePurchasedCage(Player p) {
        List<String> list = new ArrayList<>();
        for (String s : lorePurchasedCage) {
            String replaceAll = c(s)
                    .replaceAll("%player%", p.getName())
                    .replaceAll("%cage%", CageName)
                    .replaceAll("%price%", String.valueOf(priceCage)
            );
            list.add(replaceAll);
        }

        im.setLore(list);
        item.setItemMeta(im);
    }

    private void replaceLoreSelectedCage(Player p) {
        List<String> list = loreSelectedCage.stream().map(s -> c(s)
                .replaceAll("%player%", p.getName())
                .replaceAll("%cage%", CageName)
                .replaceAll("%price%", String.valueOf(priceCage)))
        .collect(Collectors.toList());

        im.setLore(list);
        item.setItemMeta(im);
    }

    public ItemStack build(Player p) {
        GamePlayer gamePlayer = Main.getPlugin().getPlayerManager().getPlayer(p.getUniqueId());

        if (im.getDisplayName() != null){
            this.replaceName(p);
        }

        if (permissionViewItem != null && !hasPerm(p)){
            return permissionViewItem;
        }

        if (im.getLore() != null) {
            this.replaceLore(p);
        }

        String invName = p.getOpenInventory().getTitle();
        String invKit = "§r"+Main.getPlugin().getConfigUtils().getConfig(Main.getPlugin(),"Kits/kits").getString("menu-settings.name").replace("&","§");
        String invCage = "§r"+Main.getPlugin().getConfigUtils().getConfig(Main.getPlugin(),"Cages/cages").getString("menu-settings.name").replace("&","§");

        if (lorePurchasedKit != null && invName.equals(invKit.replace("{type}", "Solo")) && gamePlayer.getKitSoloList().contains(kitName)){
            this.replaceLorePurchasedKit(p);
        } else if (lorePurchasedKit != null && invName.equals(invKit.replace("{type}", "Team")) &&  gamePlayer.getKitTeamList().contains(kitName)){
            this.replaceLorePurchasedKit(p);
        }

        if (kitName != null && !kitName.isEmpty()) {
            if (gamePlayer.getKitSolo().equalsIgnoreCase(kitName) && invName.equals(invKit.replace("{type}", "Solo"))) {
                this.replaceLoreSelectedKit(p);
                item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
            } else if (gamePlayer.getKitTeam().equalsIgnoreCase(kitName) && invName.equals(invKit.replace("{type}", "Team"))){
                this.replaceLoreSelectedKit(p);
                item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
            }
        }

        if (lorePurchasedCage != null && invName.equals(invCage.replace("{type}", "Solo")) && gamePlayer.getCagesSoloList().contains(CageName)){
            this.replaceLorePurchasedCage(p);
        } else if (lorePurchasedCage != null && invName.equals(invCage.replace("{type}", "Team")) && gamePlayer.getCagesTeamList().contains(CageName)){
            this.replaceLorePurchasedCage(p);
        }

        if (CageName != null && !CageName.isEmpty()) {
            if (gamePlayer.getCageSolo().equalsIgnoreCase(CageName) && invName.equals(invCage.replace("{type}", "Solo"))) {
                this.replaceLoreSelectedCage(p);
                item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                System.out.println("SelectedCage solo");
            } else if (gamePlayer.getCageTeam().equalsIgnoreCase(CageName) && invName.equals(invCage.replace("{type}", "Team"))) {
                this.replaceLoreSelectedCage(p);
                item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                System.out.println("SelectedCage team");
            }
        }

        if (this.skullOwner != null && im instanceof SkullMeta) {
            item.setType(Material.SKULL_ITEM);
            SkullMeta skullMeta = (SkullMeta)im;
            if (this.skullOwner.equalsIgnoreCase("%player%")) {
                skullMeta.setOwner(p.getName());
            }
            else {
                if (skullOwner.length() <= 16) {
                    skullMeta.setOwner(this.skullOwner);
                } else {
                    GameProfile profile = new GameProfile(UUID.randomUUID(), null);

                    profile.getProperties().put("textures", new Property("textures", skullOwner));
                    try {
                        Field profileField = skullMeta.getClass().getDeclaredField("profile");
                        profileField.setAccessible(true);
                        profileField.set(skullMeta, profile);
                    }
                    catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException ignored) { }

                }
            }
        }

        return replace(p, item);
    }

    public boolean hasPerm(Player player) {
        return permissionView == null || permissionView.length() == 0 || player.hasPermission(permissionView);
    }

    public String replace(Player player, String content) {
        return PlaceholderAPI.setPlaceholders(player, content);
    }

    public List<String> replace(Player player, List<String> content) {
        return PlaceholderAPI.setPlaceholders(player, content);
    }

    public ItemStack replace(Player player, ItemStack item) {
        if (item == null) {
            return null;
        }
        if (!item.hasItemMeta()) {
            return item;
        }
        ItemStack newItem = item.clone();
        ItemMeta itemMeta = newItem.getItemMeta();

        if (itemMeta != null) {
            if (itemMeta.hasDisplayName()) {
                itemMeta.setDisplayName(replace(player, itemMeta.getDisplayName()));
            }
            if (itemMeta.hasLore()) {
                List<String> test = replace(player, itemMeta.getLore());
                if (test != null) {
                    itemMeta.setLore(test);
                }
            }
            newItem.setItemMeta(itemMeta);
        }

        return newItem;
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
