package com.dazzhub.skywars.Listeners.Arena.onSettings;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.typeJoin.addPlayerEvent;
import com.dazzhub.skywars.Listeners.Custom.typeJoin.removePlayerEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.scoreboard.ScoreBoardAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class removePlayer implements Listener {

    private Main main;

    public removePlayer(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onRemovePlayer(removePlayerEvent e){
        GamePlayer gamePlayer = e.getGamePlayer();
        Player p = gamePlayer.getPlayer();
        Arena arena = e.getArena();

        gamePlayer.setArena(null);
        gamePlayer.setSpectating(false);
        gamePlayer.setKillsStreak(0);

        if (gamePlayer.getArenaTeam() != null) {
            gamePlayer.getArenaTeam().removeTeam(gamePlayer);
        }

        arena.getPlayers().remove(gamePlayer);
        arena.getSpectators().remove(gamePlayer);

        Bukkit.getScheduler().runTask(main, () -> {
            gamePlayer.resetPlayer(true);

            main.getItemManager().giveItems(p, "lobby", false);

            if (arena.getMode().equals(Enums.Mode.SOLO)) {
                if (gamePlayer.getArenaTeam() != null) {
                    arena.removeCage(gamePlayer, arena.getMode(), 3);
                }
            }

            gamePlayer.getPlayer().teleport(main.getLobbyManager().getLobby());

            main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), ScoreBoardAPI.ScoreboardType.LOBBY);
        });

        main.getPlayerLobby().add(p.getUniqueId());
        if (gamePlayer.getHolograms() != null) gamePlayer.getHolograms().reloadHologram();
    }
}
