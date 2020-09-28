package com.dazzhub.skywars.Listeners.Arena.onArena;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.WinEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.cryptomorin.xseries.messages.Titles;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class onWin implements Listener {

    private Main main;

    public onWin(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onWinArena(WinEvent e){
        Arena arena = e.getArena();
        arena.setGameStatus(Enums.GameStatus.RESTARTING);

        arena.getPlayers().stream().filter(gamePlayer -> !arena.getPlayers().isEmpty() && gamePlayer.getPlayer() != null).forEach(gamePlayer -> {
            Titles.sendTitle(gamePlayer.getPlayer(),
                    gamePlayer.getLangMessage().getInt("Messages.WinnerTitle.Fade"),
                    gamePlayer.getLangMessage().getInt("Messages.WinnerTitle.Stay"),
                    gamePlayer.getLangMessage().getInt("Messages.WinnerTitle.Out"),
                    c(gamePlayer.getLangMessage().getString("Messages.WinnerTitle.Info").split(";")[0]).replaceAll("%player%", gamePlayer.getPlayer().getName()),
                    c(gamePlayer.getLangMessage().getString("Messages.WinnerTitle.Info").split(";")[1]).replaceAll("%player%", gamePlayer.getPlayer().getName())
            );

            arena.getWinners(gamePlayer);

            if (arena.getMode().equals(Enums.Mode.SOLO)) {
                gamePlayer.addWinsSolo();
            } else if (arena.getMode().equals(Enums.Mode.TEAM)) {
                gamePlayer.addWinsTeam();
            }
        });

        arena.getSpectators().forEach(arena::getWinners);

        arena.getEndGameTask().runTaskTimerAsynchronously(main, 0, 20L);
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
