package com.dazzhub.skywars.Listeners.Bukkit;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.Tools;
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

        main.getServer().getScheduler().runTaskAsynchronously(main, () -> {
            main.getPlayerDB().loadPlayer(p.getUniqueId());

            Bukkit.getScheduler().runTask(main, () -> {

                if (main.getLobbyManager().getLobby() != null){
                    p.teleport(main.getLobbyManager().getLobby());
                }

                if (main.getSettings().getStringList("lobbies.onItemJoin").contains(p.getWorld().getName())) {
                    main.getItemManager().giveItems(p, main.getSettings().getString("Inventory.Lobby"), true);
                }

                main.getScoreBoardAPI().setScoreBoard(p, Enums.ScoreboardType.LOBBY,false,false, false,false);
                main.getHologramsManager().loadHologram(p);

            });
        });


        if (p.hasPermission("skywars.admin") && main.getSettings().getBoolean("checkVersion")) main.checkVersionPlayer(p);
        if (p.getName().equals("DazzHub")) p.sendMessage(c("&d&l➠ &fDazzWars use"));
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

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}

