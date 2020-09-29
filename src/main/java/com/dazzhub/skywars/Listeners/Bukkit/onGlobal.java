package com.dazzhub.skywars.Listeners.Bukkit;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.JoinEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class onGlobal implements Listener {

    private Main main;

    public onGlobal(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();

        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
        Configuration config = gamePlayer.getLangMessage();

        if (e.getView().getTitle().equalsIgnoreCase(c(config.getString("Messages.MenuArena.TITLE")))) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    int i = 0;
                    for (Arena arena : main.getArenaManager().getArenas().values()) {
                        e.getInventory().setItem(i, main.getArenasMenu().setItem(p, arena.getGameStatus(), arena));
                        if (i < config.getInt("Messages.MenuArena.ROWS") * 9 - 9) {
                            i++;
                        }
                    }

                    if (!e.getView().getTitle().equalsIgnoreCase(c(config.getString("Messages.MenuArena.TITLE")))) {
                        this.cancel();
                    }
                }
            }.runTaskTimerAsynchronously(main, 0, 20);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();

        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
        Configuration config = gamePlayer.getLangMessage();

        if (e.getView().getTitle().startsWith(c(config.getString("Messages.MenuArena.TITLE")))) {

            e.setCancelled(true);

            for (int i = 0; i < main.getArenaManager().getArenas().size(); i++) {
                if (e.getSlot() == i) {
                    Arena arena = main.getArenaManager().getArenas().get(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                    if (arena == null) return;

                    if (arena.checkUsable()) {
                        JoinEvent joinEvent = new JoinEvent(p, arena, Enums.JoinCause.MENU);
                        Bukkit.getPluginManager().callEvent(joinEvent);
                    }
                }
            }

            if (e.getSlot() == Main.getRelativePosition(config.getInt("Messages.MenuArena.Close.POSITION-X"), config.getInt("Messages.MenuArena.Close.POSITION-Y"))) {
                p.closeInventory();
            }

        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(player.getUniqueId());

        if (gamePlayer.isInArena()){
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
            p.showPlayer(player);
            player.showPlayer(p);
        });

        worldfrom.getPlayers().forEach(p -> {
            p.hidePlayer(player);
            player.hidePlayer(p);
        });
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
