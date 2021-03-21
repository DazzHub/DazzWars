package com.dazzhub.skywars.Listeners.Arena.onArena;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.WinEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Enums;
import com.cryptomorin.xseries.messages.Titles;
import org.bukkit.Bukkit;
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

        arena.getPlayers().stream().filter(gamePlayer -> gamePlayer.getPlayer() != null && !arena.getPlayers().isEmpty()).forEach(gamePlayer -> {

            Titles.sendTitle(gamePlayer.getPlayer(),
                    gamePlayer.getLangMessage().getInt("Messages.WinnerTitle.Fade",20),
                    gamePlayer.getLangMessage().getInt("Messages.WinnerTitle.Stay",20),
                    gamePlayer.getLangMessage().getInt("Messages.WinnerTitle.Out",20),
                    c(gamePlayer.getLangMessage().getString("Messages.WinnerTitle.Info", "Error title").split(";")[0]).replaceAll("%player%", gamePlayer.getPlayer().getName()),
                    c(gamePlayer.getLangMessage().getString("Messages.WinnerTitle.Info", "Error subtitle").split(";")[1]).replaceAll("%player%", gamePlayer.getPlayer().getName())
            );

            arena.getWinners(gamePlayer);

            switch (arena.getMode()) {
                case SOLO: {
                    gamePlayer.addWinsSolo();
                    gamePlayer.addCoins(main.getSettings().getDouble("Coins.WinSolo", 1));
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.GiveCoins", "Error Messages.GiveCoins").replace("%coins%", String.valueOf(main.getSettings().getInt("Coins.WinSolo", 1))));
                    main.getScoreBoardAPI().setScoreBoard(gamePlayer.getPlayer(), Enums.ScoreboardType.FINISHED, true, false, true, true);
                    break;
                }
                case TEAM: {
                    gamePlayer.addWinsTeam();
                    gamePlayer.addCoins(main.getSettings().getDouble("Coins.WinTeam", 1));
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.GiveCoins", "Error Messages.GiveCoins").replace("%coins%", String.valueOf(main.getSettings().getInt("Coins.WinTeam", 1))));
                    main.getScoreBoardAPI().setScoreBoard(gamePlayer.getPlayer(), Enums.ScoreboardType.FINISHEDTEAM, true, false, true, true);
                    break;
                }
                case RANKED: {
                    gamePlayer.addWinsRanked();
                    gamePlayer.addCoins(main.getSettings().getDouble("Coins.WinRanked", 1));
                    gamePlayer.addRanked(main.getSettings().getInt("Coins.lvlRanked", 1));
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.GiveCoins", "Error Messages.GiveCoins").replace("%coins%", String.valueOf(main.getSettings().getInt("Coins.lvlRanked", 1))));
                    main.getScoreBoardAPI().setScoreBoard(gamePlayer.getPlayer(), Enums.ScoreboardType.FINISHEDRANKED, true, false, true, true);
                    break;
                }
            }

            main.getAchievementManager().checkPlayer(gamePlayer.getPlayer(), Enums.AchievementType.WINS, gamePlayer.totalWins());
        });

        arena.getSpectators().forEach(gamePlayer -> {
            switch (arena.getMode()) {
                case SOLO: {
                    main.getScoreBoardAPI().setScoreBoard(gamePlayer.getPlayer(), Enums.ScoreboardType.FINISHED, true, false, true, true);
                    break;
                }
                case TEAM: {
                    main.getScoreBoardAPI().setScoreBoard(gamePlayer.getPlayer(), Enums.ScoreboardType.FINISHEDTEAM, true, false, true, true);
                    break;
                }
                case RANKED: {
                    main.getScoreBoardAPI().setScoreBoard(gamePlayer.getPlayer(), Enums.ScoreboardType.FINISHEDRANKED, true, false, true, true);
                    break;
                }
            }
        });


        arena.getSpectators().forEach(arena::getWinners);

        arena.endGame();
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
