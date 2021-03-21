package com.dazzhub.skywars.Utils.inventory.menu;

import com.dazzhub.skywars.Listeners.Custom.ClickMenu;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.inventory.ordItems;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.HashMap;

@Getter
@Setter
public class IMenu implements Listener {

    private Main main;
    private Inventory inv;

    private String name;
    private Integer rows;
    private String command;

    private HashMap<Integer, ordItems> itemsList;

    public IMenu(String name, Integer rows, String command, HashMap<Integer, ordItems> items) {
        this.main = Main.getPlugin();
        this.name = name;
        this.rows = rows;
        this.command = command;
        this.itemsList = items;

        Bukkit.getServer().getPluginManager().registerEvents(this, Main.getPlugin());
    }

    public void open(Player p){
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        this.inv = Bukkit.createInventory(p, rows*9, c(name));

        addItems(p);

        p.openInventory(inv);
        gamePlayer.setMenu(this);
    }

    public void addItems(Player p){
        itemsList.values().forEach(item -> inv.setItem(item.getSlot(), hideAttributes(item.getIcon().build(p))));
    }

    public boolean hasPerm(Player player, String perm) {
        return perm == null || perm.length() == 0 || player.hasPermission(perm);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (this.inv == null) return;
        if (!e.getInventory().equals(this.inv)) return;
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        int slot = e.getSlot();

        ordItems ordItems = itemsList.get(slot);
        if (ordItems == null) return;

        if (slot == ordItems.getSlot()) {
            if (hasPerm(p, ordItems.getPermission())) {
                Bukkit.getPluginManager().callEvent(new ClickMenu(gamePlayer, ordItems));
            } else {
                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.menu-deny"));
            }
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onLeft(InventoryCloseEvent e) {
        if (this.inv == null) return;
        if (!e.getInventory().equals(this.inv)) return;
        if (!(e.getPlayer() instanceof Player)) return;

        Player p = (Player) e.getPlayer();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (e.getInventory().equals(this.inv)) {
            gamePlayer.setMenu(null);
        }
    }

    @EventHandler
    public void onDragInv(InventoryDragEvent e){
        if (this.inv == null) return;
        if (!e.getInventory().equals(this.inv)) return;
        if (!(e.getWhoClicked() instanceof Player)) return;

        e.setCancelled(true);
    }

    public ItemStack hideAttributes(ItemStack item) {
        if (item == null) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        if (isNullOrEmpty(meta.getItemFlags())) {
            meta.addItemFlags(ItemFlag.values());
            item.setItemMeta(meta);
        }
        return item;
    }

    private boolean isNullOrEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
