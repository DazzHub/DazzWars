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

        if (arena.getRefillGame() != null) {
            arena.getRefillGame().cancel();
            arena.setRefillGame(null);
            arena.getRefillTime().clear();
        }

        if (arena.getEventBorder() != null) arena.getEventBorder().stopTimer();
        if (arena.getEventDragon() != null) arena.getEventDragon().killDragon();
        if (arena.getEventParty() != null) arena.getEventParty().stopTimer();
        if (arena.getEventStorm() != null) arena.getEventStorm().stopTimer();
        if (arena.getEventTNT() != null) arena.getEventTNT().stopEvent();

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
                gamePlayer.addCoins(main.getSettings().getInt("Coins.WinSolo"));
                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.GiveCoins").replace("%coins%", String.valueOf(main.getSettings().getInt("Coins.WinSolo"))));
            } else if (arena.getMode().equals(Enums.Mode.TEAM)) {
                gamePlayer.addWinsTeam();
                gamePlayer.addCoins(main.getSettings().getInt("Coins.WinTeam"));
                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.GiveCoins").replace("%coins%", String.valueOf(main.getSettings().getInt("Coins.WinTeam"))));
            } else if (arena.getMode().equals(Enums.Mode.RANKED)) {
                gamePlayer.addWinsRanked();
                gamePlayer.addCoins(main.getSettings().getInt("Coins.WinRanked"));
                gamePlayer.addRanked(main.getSettings().getInt("Coins.lvlRanked"));
                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.GiveCoins").replace("%coins%", String.valueOf(main.getSettings().getInt("Coins.lvlRanked"))));
            }

        });

        arena.getSpectators().forEach(arena::getWinners);

        arena.getEndGameTask().runTaskTimer(main, 0, 20L);
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
