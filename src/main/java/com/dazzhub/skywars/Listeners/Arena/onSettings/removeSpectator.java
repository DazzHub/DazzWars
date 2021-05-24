package com.dazzhub.skywars.Listeners.Arena.onSettings;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.typeJoin.removeSpectatorEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class removeSpectator implements Listener {

    private final Main main;

    public removeSpectator(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onRemoveSpectator(removeSpectatorEvent e){
        GamePlayer gamePlayer = e.getGamePlayer();
        Player p = gamePlayer.getPlayer();
        Arena arena = e.getArena();

        //Bukkit.getScheduler().runTask(main, () -> {
            gamePlayer.resetPlayer(true);

            if (e.isGoLobby()) {
                gamePlayer.setLobby(true);
                p.teleport(main.getLobbyManager().getLobby());

                if (!arena.getGameStatus().equals(Enums.GameStatus.RESTARTING)) {
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.LeaveSpectator", "Error Messages.LeaveSpectator"));
                }

                main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), Enums.ScoreboardType.LOBBY,false,false,false, false);

                main.getItemManager().getItemLangs().get(gamePlayer.getLang()).giveItems(p, main.getSettings().getString("Inventory.Lobby", "lobby"), false);

                if (gamePlayer.getHolograms() != null) gamePlayer.getHolograms().reloadHologram();
            } else {
                gamePlayer.setLobby(false);
            }
        //});

        gamePlayer.setArena(null);
        gamePlayer.setSpectating(false);
        gamePlayer.setKillsStreak(0);

        arena.getSpectators().remove(gamePlayer);
        arena.getPlayers().remove(gamePlayer);

    }

}
