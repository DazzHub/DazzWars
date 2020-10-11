package com.dazzhub.skywars.Arena.Menu;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.inventory.Icon;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SpectatorMenu {

    private Main main;
    private Arena arena;

    public SpectatorMenu(Arena arena) {
        this.main = Main.getPlugin();
        this.arena = arena;
    }

    public void menuPlayers(Player player){
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(player.getUniqueId());
        Configuration config = gamePlayer.getLangMessage();

        Inventory inv = Bukkit.createInventory(null, config.getInt("Messages.MenuSpectator.ROWS") * 9, c(config.getString("Messages.MenuSpectator.TITLE")));

        if (inv == null) return;

        List<String> lore = config.getStringList("Messages.MenuSpectator.Close.DESCRIPTION");

        Bukkit.getScheduler().runTaskAsynchronously(main, ()-> {
            for (GamePlayer on : arena.getPlayers()) {
                inv.addItem(new Icon(XMaterial.PLAYER_HEAD)
                        .setName(config.getString("Messages.MenuSpectator.Player.NAME", "Error: MenuSpectator.Player.NAME"))
                        .setLore(config.getStringList("Messages.MenuSpectator.Player.DESCRIPTION"))
                        .setSkull(on.getPlayer().getName())
                        .build(on.getPlayer())
                );
            }
        });

        Material material = config.isInt("Messages.MenuSpectator.Close.ICON-ITEM") ? Material.getMaterial(config.getInt("Messages.MenuSpectator.Close.ICON-ITEM")) : Material.getMaterial(config.getString("Messages.MenuSpectator.Close.ICON-ITEM"));
        inv.setItem(Main.getRelativePosition(config.getInt("Messages.MenuSpectator.Close.POSITION-X"), config.getInt("Messages.MenuSpectator.Close.POSITION-Y")), new Icon(XMaterial.matchXMaterial(material),1, (short) config.getInt("Messages.MenuSpectator.Close.DATA-VALUE")).setName(config.getString("Messages.MenuSpectator.Close.NAME")).setLore(lore).build(player));

        player.openInventory(inv);
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
