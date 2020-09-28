package com.dazzhub.skywars.Listeners.Arena.onSettings;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.typeJoin.addSpectatorEvent;
import com.dazzhub.skywars.Listeners.Custom.typeJoin.removeSpectatorEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.scoreboard.ScoreBoardAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class removeSpectator implements Listener {

    private Main main;

    public removeSpectator(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onRemoveSpectator(removeSpectatorEvent e){
        GamePlayer gamePlayer = e.getGamePlayer();
        Player p = gamePlayer.getPlayer();
        Arena arena = e.getArena();

        Bukkit.getScheduler().runTask(main, () -> {
            gamePlayer.resetPlayer(true);

            if (e.isGoLobby()) {
                gamePlayer.setLobby(true);

                if (!arena.getGameStatus().equals(Enums.GameStatus.RESTARTING)) {
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.LeaveSpectator"));
                }

                main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), ScoreBoardAPI.ScoreboardType.LOBBY,false,false,false, false);
                main.getItemManager().giveItems(p, "lobby", false);

                p.teleport(main.getLobbyManager().getLobby());
                if (gamePlayer.getHolograms() != null) gamePlayer.getHolograms().reloadHologram();
            } else {
                gamePlayer.setLobby(false);
            }
        });

        gamePlayer.setArena(null);
        gamePlayer.setSpectating(false);
        gamePlayer.setKillsStreak(0);

        arena.getSpectators().remove(gamePlayer);
        arena.getPlayers().remove(gamePlayer);

    }

}
