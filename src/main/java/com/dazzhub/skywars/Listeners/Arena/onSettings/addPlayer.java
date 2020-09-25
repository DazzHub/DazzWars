package com.dazzhub.skywars.Listeners.Arena.onSettings;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.typeJoin.addPlayerEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Runnables.startingGame;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.scoreboard.ScoreBoardAPI;
import com.dazzhub.skywars.Utils.xseries.Titles;
import org.bukkit.Bukkit;
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
    public void onAddPlayer(addPlayerEvent e){
        GamePlayer gamePlayer = e.getGamePlayer();
        Player p = gamePlayer.getPlayer();

        Arena arena = e.getArena();

        if (gamePlayer.isInArena()) return;

        if (gamePlayer.getArenaTeam() != null) {
            gamePlayer.getArenaTeam().removeTeam(gamePlayer);
        }

        main.getPlayerLobby().remove(p.getUniqueId());

        gamePlayer.setArena(arena);
        gamePlayer.setSpectating(false);
        arena.getPlayers().add(gamePlayer);

        Bukkit.getScheduler().runTask(main, () -> {
            gamePlayer.resetPlayer(true);

            switch (arena.getMode()) {
                case SOLO: {
                    arena.getAvaibleTeam(arena.getMode().getSize()).addPlayer(gamePlayer);

                    Location cageLoc = gamePlayer.getArenaTeam().getSpawn();
                    p.teleport(cageLoc);

                    main.getCageManager().getCagesSolo().get(gamePlayer.getCageSolo()).loadCage(cageLoc);
                    main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), ScoreBoardAPI.ScoreboardType.STARTING);

                    main.getItemManager().giveItems(p, "arenasolo", false);
                    break;
                }
                case TEAM: {
                    arena.getRandomTeam().addPlayer(gamePlayer);

                    Location cageLoc = gamePlayer.getArenaTeam().getSpawn();
                    p.teleport(cageLoc);

                    main.getCageManager().getCagesTeam().get(gamePlayer.getCageTeam()).loadCage(cageLoc);
                    main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), ScoreBoardAPI.ScoreboardType.STARTINGTEAM);

                    main.getItemManager().giveItems(p, "arenateam", false);
                    break;
                }
            }
        });

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
                gamePlayer.getLangMessage().getString("Messages.JoinTitle.Info").split(";")[0].replaceAll("%player%", p.getName()).replace("%mode%", arena.getMode().toString()),
                gamePlayer.getLangMessage().getString("Messages.JoinTitle.Info").split(";")[1].replaceAll("%player%", p.getName()).replace("%mode%", arena.getMode().toString())
        );

        if (arena.checkStart() && !arena.isUsable()) {
            arena.getStartingGameTask().runTaskTimerAsynchronously(main, 0, 20L);

            arena.setUsable(true);
            arena.setGameStatus(Enums.GameStatus.STARTING);
        }
    }
}
