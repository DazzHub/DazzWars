package com.dazzhub.skywars.Listeners.Arena.onSettings;

import com.cryptomorin.xseries.messages.Titles;
import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.typeJoin.addPlayerEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.locUtils;
import com.dazzhub.skywars.Utils.scoreboard.ScoreBoardAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class addPlayer implements Listener {

    private Main main;

    public addPlayer(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onAddPlayer(addPlayerEvent e) {
        GamePlayer gamePlayer = e.getGamePlayer();
        Player p = gamePlayer.getPlayer();

        Arena arena = e.getArena();

        if (gamePlayer.isInArena()) return;

        if (gamePlayer.getArenaTeam() != null) {
            gamePlayer.getArenaTeam().removeTeam(gamePlayer);
        }

        gamePlayer.setArena(arena);
        gamePlayer.setSpectating(false);
        gamePlayer.setLobby(false);
        arena.getPlayers().add(gamePlayer);


        gamePlayer.resetPlayer(true);

        switch (arena.getMode()) {
            case SOLO: {
                arena.getAvailableTeam(arena.getMode().getSize()).addPlayer(gamePlayer);

                Location cageLoc = gamePlayer.getArenaTeam().getSpawn();
                p.teleport(cageLoc);

                main.getCageManager().getCagesSolo().get(gamePlayer.getCageSolo()).loadCage(cageLoc);
                main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), Enums.ScoreboardType.STARTING, false, false, true, true);

                main.getItemManager().giveItems(p, main.getSettings().getString("Inventory.Arena.Solo"), false);
                break;
            }
            case TEAM: {
                arena.getRandomTeam().addPlayer(gamePlayer);

                Location cageLoc = gamePlayer.getArenaTeam().getSpawn();
                p.teleport(cageLoc);

                main.getCageManager().getCagesTeam().get(gamePlayer.getCageTeam()).loadCage(cageLoc);
                main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), Enums.ScoreboardType.STARTINGTEAM, false, false, true, true);

                main.getItemManager().giveItems(p, main.getSettings().getString("Inventory.Arena.Team"), false);
                break;
            }

            case RANKED: {
                arena.getRandomTeam().addPlayer(gamePlayer);

                Location cageLoc = gamePlayer.getArenaTeam().getSpawn();
                p.teleport(cageLoc);

                main.getCageManager().getCagesRanked().get(gamePlayer.getCageRanked()).loadCage(cageLoc);
                main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), Enums.ScoreboardType.STARTINGRANKED, false, false, true, true);

                main.getItemManager().giveItems(p, main.getSettings().getString("Inventory.Arena.Ranked"), false);
                break;
            }
        }


        arena.getPlayers().forEach(playerArena ->
                playerArena.sendMessage(playerArena.getLangMessage().getString("Messages.JoinMessage")
                        .replaceAll("%player%", p.getName())
                        .replaceAll("%playing%", String.valueOf(arena.getPlayers().size()))
                        .replaceAll("%max%", String.valueOf(arena.getMaxPlayers()))
                )
        );

        Titles.sendTitle(p.getPlayer(),
                gamePlayer.getLangMessage().getInt("Messages.JoinTitle.Fade"),
                gamePlayer.getLangMessage().getInt("Messages.JoinTitle.Stay"),
                gamePlayer.getLangMessage().getInt("Messages.JoinTitle.Out"),
                c(gamePlayer.getLangMessage().getString("Messages.JoinTitle.Info").split(";")[0]).replaceAll("%player%", p.getName()).replace("%mode%", arena.getMode().toString()),
                c(gamePlayer.getLangMessage().getString("Messages.JoinTitle.Info").split(";")[1]).replaceAll("%player%", p.getName()).replace("%mode%", arena.getMode().toString())
        );

        if (arena.checkStart() && !arena.isUsable()) {
            arena.getStartingGameTask().runTaskTimer(main, 0, 20L);

            arena.setUsable(true);
            arena.setGameStatus(Enums.GameStatus.STARTING);
        }
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
