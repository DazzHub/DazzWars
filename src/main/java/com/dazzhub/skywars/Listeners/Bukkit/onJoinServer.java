package com.dazzhub.skywars.Listeners.Bukkit;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.scoreboard.ScoreBoardAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onJoinServer implements Listener {

    private Main main;

    public onJoinServer(Main main) {
        this.main = main;
    }

    @EventHandler
    public void Join(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        main.getServer().getScheduler().runTaskAsynchronously(main, () -> main.getPlayerDB().loadPlayer(p.getUniqueId()));

        if (main.getLobbyManager().getLobby() != null){
            p.teleport(main.getLobbyManager().getLobby());
        }

        main.getItemManager().giveItems(p, "lobby", true);

        Bukkit.getScheduler().runTaskLater(main, () -> {
            main.getScoreBoardAPI().setScoreBoard(p, ScoreBoardAPI.ScoreboardType.LOBBY);
            main.getHologramsManager().loadHologram(p);
        },5);

        if (p.hasPermission("dazzwars.admin") && main.getSettings().getBoolean("checkVersion")) main.checkVersionPlayer(p);
        if (p.getName().equals("DazzHub")) p.sendMessage(c("&d&l➠ &fDazzWars use"));
    }

    @EventHandler
    public void Join2(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        Bukkit.getServer().getOnlinePlayers().forEach(p -> {
            if (p.getWorld() == world) {
                p.showPlayer(player);
                player.showPlayer(p);
            } else {
                p.hidePlayer(player);
                player.hidePlayer(p);
            }
        });
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
