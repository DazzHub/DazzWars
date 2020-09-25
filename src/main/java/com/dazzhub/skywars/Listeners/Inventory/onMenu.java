package com.dazzhub.skywars.Listeners.Inventory;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.inventory.menu.IMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class onMenu implements Listener {

    private Main main;

    public onMenu(Main main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        String invName = event.getView().getTitle();

        if (invName.contains("/")) {
            invName = invName.split("/")[0];
        }

        IMenu menu = main.getMenuManager().getMenuTileName().get(invName);
        if (menu == null) return;

        menu.onInventoryClick(event);
        if(event.getView().getTopInventory().equals(event.getInventory())) event.setCancelled(true);
        if(event.getView().getBottomInventory().equals(event.getInventory())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        String invName = event.getView().getTitle();

        if (invName.contains("/")) {
            invName = invName.split("/")[0];
        }

        IMenu menu = main.getMenuManager().getMenuTileName().get(invName);
        if (menu == null) return;

        menu.closeInv((Player) event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void handleCommandEvent(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage();
        cmd = cmd.substring(1);

        if (cmd.length() == 0) {
            return;
        }

        String file = this.main.getMenuManager().getMenuCommand().get(cmd.toLowerCase());
        if (file != null) {
            e.setCancelled(true);
            Player p = e.getPlayer();
            this.main.getMenuManager().openInventory(file, p, null);
        }
    }
}
