package com.dazzhub.skywars.Listeners.Bukkit;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.JoinEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class onGlobal implements Listener {

    private final Main main;

    public onGlobal(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClickSave(InventoryClickEvent e) {
        String nameinv = e.getView().getTitle();
        if (!nameinv.contains("/")) return;

        String[] inv = nameinv.split("/");
        if (inv[0].startsWith(c("editChest"))) {
            Player p = (Player) e.getWhoClicked();

            if (e.getSlot() == 53) {
                main.getChestManager().save(inv[1], p);

                p.closeInventory();
                e.setCancelled(true);
            }

            if (e.getSlot() == 52) {
                p.closeInventory();
                e.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(player.getUniqueId());

        if (gamePlayer != null && gamePlayer.isInArena()){
            if (gamePlayer.getArena().getGameStatus().equals(Enums.GameStatus.WAITING) || gamePlayer.getArena().getGameStatus().equals(Enums.GameStatus.STARTING)){
                if (!player.hasPermission("skywars.chat")){
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.NoUseChat"));
                    e.setCancelled(true);
                }
            }
        }

        Bukkit.getServer().getOnlinePlayers().stream().filter(player2 -> player2.getWorld() != player.getWorld()).forEach(player2 -> e.getRecipients().remove(player2));

    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        World worldfrom = event.getFrom();

        world.getPlayers().forEach(p -> {
            Tools.ShowPlayer(p, player);
            Tools.ShowPlayer(player, p);
        });

        worldfrom.getPlayers().forEach(p -> {
            Tools.HidePlayer(p, player);
            Tools.HidePlayer(player, p);
        });
    }

    @EventHandler
    public void onSaveWorld(WorldSaveEvent e){
        List<String> arenaslist = main.getConfigUtils().getConfig(this.main, "Arenas/Arenas").getStringList("Arenas");

        arenaslist.forEach(nameArena -> {
            if (nameArena.equalsIgnoreCase(e.getWorld().getName())){
                e.getWorld().setAutoSave(false);
            }
        });

    }


    @EventHandler
    public void onClickInv(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();
        if (main.getPlayerManager().getPlayer(p.getUniqueId()).isEditMode()) return;
        if (!main.getSettings().getStringList("lobbies.onItemJoin").contains(p.getWorld().getName())) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onDragInv(InventoryDragEvent e){
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();
        if (main.getPlayerManager().getPlayer(p.getUniqueId()).isEditMode()) return;
        if (!main.getSettings().getStringList("lobbies.onItemJoin").contains(p.getWorld().getName())) return;

        e.setCancelled(true);
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
