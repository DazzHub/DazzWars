package com.dazzhub.skywars.Arena.Menu;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.inventory.Icon;
import com.dazzhub.skywars.Utils.xseries.XMaterial;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class ArenasMenu
{
    private Main main;
    private Inventory inv;

    public ArenasMenu(Main main) {
        this.main = main;
    }

    public void open(Player p) {
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
        Configuration config = gamePlayer.getLangMessage();

        this.inv = Bukkit.createInventory(p, gamePlayer.getLangMessage().getInt("Messages.MenuArena.ROWS") * 9, this.c(gamePlayer.getLangMessage().getString("Messages.MenuArena.TITLE")));

        Material material = config.isInt("Messages.MenuArena.Close.ICON-ITEM") ? Material.getMaterial(config.getInt("Messages.MenuArena.Close.ICON-ITEM")) : Material.getMaterial(config.getString("Messages.MenuArena.Close.ICON-ITEM"));
        inv.setItem(Main.getRelativePosition(config.getInt("Messages.MenuArena.Close.POSITION-X"), config.getInt("Messages.MenuArena.Close.POSITION-Y")), new Icon(XMaterial.matchXMaterial(material), 1, (short) config.getInt("Messages.MenuArena.Close.DATA-VALUE")).setName(config.getString("Messages.MenuArena.Close.NAME")).setLore(config.getStringList("Messages.MenuArena.Close.DESCRIPTION")).build(p));

        p.openInventory(this.inv);
    }

    public ItemStack setItem(Player p, Enums.GameStatus color, Arena arena) {
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
        Configuration config = gamePlayer.getLangMessage();

        if (!config.getBoolean("Messages.MenuArena.CustomItem.Use")) {
            switch (color){
                case WAITING:{
                    return itemCharge(p, arena, Color.GREEN);
                }
                case STARTING:{
                    return itemCharge(p, arena, Color.YELLOW);
                }
                case INGAME:{
                    return itemCharge(p, arena, Color.RED);
                }
                case RESTARTING:{
                    return itemCharge(p, arena, Color.BLUE);
                }
            }

        } else if (config.getBoolean("Messages.MenuArena.CustomItem.Use")) {
            switch (color){
                case WAITING:{
                    return itemCustom(p, arena, "WAITING");
                }
                case STARTING:{
                    return itemCustom(p, arena, "STARTING");
                }
                case INGAME:{
                    return itemCustom(p, arena, "INGAME");
                }
                case RESTARTING:{
                    return itemCustom(p, arena, "RESTARTING");
                }
            }
        }

        if (arena == null){
            return new Icon(XMaterial.BEDROCK, 1, (short) 0).setName("Arena invalid").build(p);
        }
        return new Icon(XMaterial.BEDROCK, 1, (short) 0).build(p);
    }

    private ItemStack itemCharge(Player p, Arena arena, Color color){
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
        Configuration config = gamePlayer.getLangMessage();

        List<String> arrays = config.getStringList("Messages.MenuArena.FIREWORK.DESCRIPTION");
        List<String> lore;

        ItemStack item = new ItemStack(XMaterial.FIREWORK_STAR.parseMaterial(), 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(this.c(config.getString("Messages.MenuArena.FIREWORK.NAME").replace("%arena%", arena.getNameArena())));

        lore = arrays.stream().map(s -> c(s)
                .replace("%state%", String.valueOf(arena.getGameStatus()))
                .replace("%arena%", arena.getNameArena()).replace("%mode%", arena.getMode().toString())
                .replace("%maxPlayers%", String.valueOf(arena.getMaxPlayers()))
                .replace("%online%", String.valueOf(arena.getPlayers().size()))).collect(Collectors.toList());

        meta.setLore(lore);
        FireworkEffectMeta metaFw2 = (FireworkEffectMeta) meta;
        FireworkEffect effect2 = FireworkEffect.builder().withColor(color).build();
        metaFw2.addItemFlags(ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS);
        metaFw2.setEffect(effect2);
        item.setItemMeta(metaFw2);

        return item;
    }

    private ItemStack itemCustom(Player p, Arena arena, String status){
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
        Configuration config = gamePlayer.getLangMessage();

        List<String> arrays = config.getStringList("Messages.MenuArena.CustomItem." + status + ".DESCRIPTION");
        List<String> lore = arrays.stream().map(s -> c(s)
                .replace("%state%", String.valueOf(arena.getGameStatus()))
                .replace("%mode%", arena.getMode().toString())
                .replace("%arena%", arena.getNameArena())
                .replace("%mode%", arena.getMode().toString())
                .replace("%maxPlayers%", String.valueOf(arena.getMaxPlayers()))
                .replace("%online%", String.valueOf(arena.getPlayers().size()))).collect(Collectors.toList());

        Material material = config.isInt("Messages.MenuArena.CustomItem." + status + ".ICON-ITEM") ? Material.getMaterial(config.getInt("Messages.MenuArena.CustomItem." + status + ".ICON-ITEM")) : Material.getMaterial("Messages.MenuArena.CustomItem." + status + ".ICON-ITEM");
        
        return new Icon(XMaterial.matchXMaterial(material),1, (short) config.getInt("Messages.MenuArena.CustomItem." + status + ".DATA-VALUE"))
                .setName(config.getString("Messages.MenuArena.CustomItem." + status + ".NAME").replace("%arena%", arena.getNameArena()))
                .setLore(lore)
                .build(p);
    }

    public Inventory getInv() {
        return inv;
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
