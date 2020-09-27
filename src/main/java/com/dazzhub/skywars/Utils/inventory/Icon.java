package com.dazzhub.skywars.Utils.inventory;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Console;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class Icon {

    private ItemStack item;
    private ItemMeta im;
    private String skullOwner;

    private int price;
    private String type;

    private String permissionView;
    private ItemStack permissionViewItem;

    private List<String> lorePurchased;
    private List<String> loreSelected;

    public Icon(XMaterial m, int amount, short dataValue){
        this.item = new ItemStack(m.parseMaterial(), amount != 0 ? amount : 1, dataValue);
        this.im = this.item.getItemMeta();

        this.permissionViewItem = null;

        this.price = 0;
        this.type = "";

        this.lorePurchased = null;
        this.loreSelected = null;
    }

    public Icon(XMaterial m){
        item = new ItemStack(m.parseMaterial(), 1);
        im = item.getItemMeta();
    }

    public Icon setName(String name){
        im.setDisplayName(c(name));
        item.setItemMeta(im);
        return this;
    }

    public Icon setLore(List<String> lore){
        ArrayList<String> fullLore = lore.stream().map(this::c).collect(Collectors.toCollection(ArrayList::new));

        im.setLore(fullLore);
        item.setItemMeta(im);
        return this;
    }

    public Icon setLorePurchased(List<String> lore){
        this.lorePurchased = lore;
        return this;
    }

    public Icon setLoreSelected(List<String> lore){
        this.loreSelected = lore;
        return this;
    }

    public Icon setLore(String... lore) {
        ArrayList<String> fullLore = Arrays.stream(lore).map(this::c).collect(Collectors.toCollection(ArrayList::new));

        im.setLore(fullLore);
        item.setItemMeta(im);
        return this;
    }

    public Icon setPrice(int price){
        this.price = price;
        return this;
    }

    public Icon setType(String type){
        this.type = type;
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

    public Icon addDamage(short damage) {
        if (damage == 0) return this;

        short total = (short) (item.getType().getMaxDurability() - damage);
        item.setDurability(total);
        return this;
    }

    private void replaceName(Player p) {
        im.setDisplayName(c(im.getDisplayName()).replaceAll("%player%", p.getName()));

        item.setItemMeta(im);
    }

    private void replaceName() {
        im.setDisplayName(c(im.getDisplayName()));
        item.setItemMeta(im);
    }

    private void replaceLore(GamePlayer p) {

        if (im.getLore() != null){
            List<String> list = new ArrayList<>();
            for (String s : im.getLore()) {
                list.add(c(s).replaceAll("%player%", p.getName()));
            }

            im.setLore(list);
            item.setItemMeta(im);
        }

        if (this.lorePurchased != null && type.length() != 0) {
            if (type.startsWith("kit:")) {
                String action = type.substring(4);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String kitName = action.split("/")[0];
                String mode = action.split("/")[1];

                if (mode.equalsIgnoreCase("solo") && p.getKitSoloList().contains(kitName)) {
                    replaceLorePurchased(p, kitName);
                } else if (mode.equalsIgnoreCase("team") && p.getKitTeamList().contains(kitName)) {
                    replaceLorePurchased(p, kitName);
                }
            } else if (type.startsWith("cage:")) {
                String action = type.substring(5);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String cageName = action.split("/")[0];
                String mode = action.split("/")[1];

                if (mode.equalsIgnoreCase("solo") && p.getCagesSoloList().contains(cageName)) {
                    replaceLorePurchased(p, cageName);
                } else if (mode.equalsIgnoreCase("team") && p.getCagesTeamList().contains(cageName)) {
                    replaceLorePurchased(p, cageName);
                }
            } else if (type.startsWith("wineffect:")) {
                String action = type.substring(10);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String winName = action.split("/")[0];
                String mode = action.split("/")[1];

                if (mode.equalsIgnoreCase("solo") && p.getWinEffectsSoloList().contains(winName)) {
                    replaceLorePurchased(p, winName);
                } else if (mode.equalsIgnoreCase("team") && p.getWinEffectsTeamList().contains(winName)) {
                    replaceLorePurchased(p, winName);
                }
            } else if (type.startsWith("killeffect:")) {
                String action = type.substring(11);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String killName = action.split("/")[0];
                String mode = action.split("/")[1];

                if (mode.equalsIgnoreCase("solo") && p.getKillEffectsSoloList().contains(killName)) {
                    replaceLorePurchased(p, killName);
                } else if (mode.equalsIgnoreCase("team") && p.getKillEffectsTeamList().contains(killName)) {
                    replaceLorePurchased(p, killName);
                }
            }

            else if (type.startsWith("traileffect:")) {
                String action = type.substring(12);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String trailName = action.split("/")[0];
                String mode = action.split("/")[1];

                if (mode.equalsIgnoreCase("solo") && p.getTrailsSoloList().contains(trailName)) {
                    replaceLorePurchased(p, trailName);
                } else if (mode.equalsIgnoreCase("team") && p.getTrailsTeamList().contains(trailName)) {
                    replaceLorePurchased(p, trailName);
                }
            }
        }

        if (this.loreSelected != null && type.length() != 0) {
            if (type.startsWith("kit:")) {
                String action = type.substring(4);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String kitName = action.split("/")[0];
                String mode = action.split("/")[1];

                if (mode.equalsIgnoreCase("solo") && p.getKitSolo().equals(kitName)) {
                    replaceLoreSelected(p, kitName);
                    this.item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                } else if (mode.equalsIgnoreCase("team") && p.getKitSolo().equals(kitName)) {
                    replaceLoreSelected(p, kitName);
                    this.item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                }
            } else if (type.startsWith("cage:")) {
                String action = type.substring(5);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String cageName = action.split("/")[0];
                String mode = action.split("/")[1];

                if (mode.equalsIgnoreCase("solo") && p.getCageSolo().equals(cageName)) {
                    replaceLoreSelected(p, cageName);
                    this.item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                } else if (mode.equalsIgnoreCase("team") && p.getCageTeam().equals(cageName)) {
                    replaceLoreSelected(p, cageName);
                    this.item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                }
            } else if (type.startsWith("wineffect:")) {
                String action = type.substring(10);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String winName = action.split("/")[0];
                String mode = action.split("/")[1];

                if (mode.equalsIgnoreCase("solo") && p.getWinEffectSolo().equals(winName)) {
                    replaceLoreSelected(p, winName);
                    this.item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                } else if (mode.equalsIgnoreCase("team") && p.getWinEffectTeam().equals(winName)) {
                    replaceLoreSelected(p, winName);
                    this.item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                }
            } else if (type.startsWith("killeffect:")) {
                String action = type.substring(11);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String killName = action.split("/")[0];
                String mode = action.split("/")[1];

                if (mode.equalsIgnoreCase("solo") && p.getKillEffectSolo().equals(killName)) {
                    replaceLoreSelected(p, killName);
                    this.item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                } else if (mode.equalsIgnoreCase("team") && p.getKillEffectTeam().equals(killName)) {
                    replaceLoreSelected(p, killName);
                    this.item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                }
            } else if (type.startsWith("traileffect:")) {
                String action = type.substring(12);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String trailName = action.split("/")[0];
                String mode = action.split("/")[1];

                if (mode.equalsIgnoreCase("solo") && p.getTrailSolo().equals(trailName)) {
                    replaceLoreSelected(p, trailName);
                    this.item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                } else if (mode.equalsIgnoreCase("team") && p.getTrailTeam().equals(trailName)) {
                    replaceLoreSelected(p, trailName);
                    this.item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                }
            }

        }
    }

    private void replaceLorePurchased(GamePlayer p, String name) {
        List<String> list = new ArrayList<>();
        for (String s : lorePurchased) {
            String replaceAll = c(s)
                    .replaceAll("%player%", p.getName())
                    .replaceAll("%name%", name)
                    .replaceAll("%price%", String.valueOf(price));
            list.add(replaceAll);
        }

        im.setLore(list);
        item.setItemMeta(im);
    }

    private void replaceLoreSelected(GamePlayer p, String name) {
        List<String> list = new ArrayList<>();
        for (String s : loreSelected) {
            String replaceAll = c(s)
                    .replaceAll("%player%", p.getName())
                    .replaceAll("%name%", name)
                    .replaceAll("%price%", String.valueOf(price));
            list.add(replaceAll);
        }

        im.setLore(list);
        item.setItemMeta(im);
    }

    private void replaceLore() {
        List<String> list = new ArrayList<>();

        for (String s : im.getLore()) {
            list.add(c(s));
        }

        im.setLore(list);
        item.setItemMeta(im);
    }

    public ItemStack build() {

        if (im.getDisplayName() != null) {
            this.replaceName();
        }

        if (im.getLore() != null) {
            this.replaceLore();
        }

        return item;
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
            this.replaceLore(gamePlayer);
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
                    SkullUtils.applySkin(skullMeta, this.skullOwner);
                }
            }
            item.setItemMeta(im);
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
