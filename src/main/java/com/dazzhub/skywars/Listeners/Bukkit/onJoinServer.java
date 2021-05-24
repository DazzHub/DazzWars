package com.dazzhub.skywars.Listeners.Bukkit;

import com.dazzhub.skywars.Listeners.Custom.ConnectionsEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onJoinServer implements Listener {

    private final Main main;

    public onJoinServer(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onConnections(ConnectionsEvent e) {
        GamePlayer gamePlayer = e.getGamePlayer();
        Player p = gamePlayer.getPlayer();

        if (main.getSettings().getStringList("lobbies.onJoinTp").contains(p.getWorld().getName()) && main.getLobbyManager().getLobby() != null) {
            p.teleport(main.getLobbyManager().getLobby());
        }

        main.getScoreBoardAPI().setScoreBoard(p, Enums.ScoreboardType.LOBBY, false, false, false, false);
        main.getHologramsManager().loadHologram(p);

        main.getItemManager().getItemLangs().get(gamePlayer.getLang()).giveItems(p, main.getSettings().getString("Inventory.Lobby", "lobby"), false);
    }

    @EventHandler
    public void Join(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> main.getPlayerDB().loadPlayer(p.getUniqueId()));

        if (p.hasPermission("skywars.admin") && main.getSettings().getBoolean("checkVersion"))
            main.checkVersionPlayer(p);
        if (p.getName().equals("DazzHub")) p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&l➠ &fDazzWars use"));
    }

    @EventHandler
    public void Join2(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        Bukkit.getServer().getOnlinePlayers().forEach(p -> {
            if (p.getWorld() == world) {
                Tools.ShowPlayer(p, player);
                Tools.ShowPlayer(player, p);
            } else {
                Tools.HidePlayer(p, player);
                Tools.HidePlayer(player, p);
            }
        });
    }

}

