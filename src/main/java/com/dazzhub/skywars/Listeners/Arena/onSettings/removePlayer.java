package com.dazzhub.skywars.Listeners.Arena.onSettings;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.DeathEvent;
import com.dazzhub.skywars.Listeners.Custom.typeJoin.removePlayerEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class removePlayer implements Listener {

    private final Main main;

    public removePlayer(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onRemovePlayer(removePlayerEvent e) {
        GamePlayer gamePlayer = e.getGamePlayer();
        Player p = gamePlayer.getPlayer();
        Arena arena = e.getArena();

        gamePlayer.setArena(null);
        gamePlayer.setSpectating(false);
        gamePlayer.setKillsStreak(0);

        if (gamePlayer.getArenaTeam() != null) {
            gamePlayer.getArenaTeam().removeTeam(gamePlayer);
        }

        if (!arena.getSpectators().contains(gamePlayer) && arena.getPlayers().contains(gamePlayer)){
            boolean isTagged = main.getPlayerManager().getTaggedCooldown().isTagged(p);

            main.getPlayerManager().getTaggedCooldown().removeDeath(p);
            main.getPlayerManager().getTaggedCooldown().removeTimer(p);

            if (isTagged){
                GamePlayer gameKiller = main.getPlayerManager().getPlayer(main.getPlayerManager().getTaggedCooldown().getKiller(p));

                Bukkit.getPluginManager().callEvent(new DeathEvent(gamePlayer, gameKiller, gamePlayer.getArena(), null));
            }
        }

        arena.getPlayers().remove(gamePlayer);
        arena.getSpectators().remove(gamePlayer);


        gamePlayer.resetPlayer(true);

        if (e.isGoLobby()) {
            gamePlayer.getPlayer().teleport(main.getLobbyManager().getLobby());
            gamePlayer.setLobby(true);
        }

        if (arena.getMode().equals(Enums.Mode.SOLO)) {
            if (gamePlayer.getArenaTeam() != null) {
                main.getCageManager().getCagesSolo().get(gamePlayer.getCageSolo()).removeCage(gamePlayer.getArenaTeam().getSpawn());
            }
        }

        main.getItemManager().getItemLangs().get(gamePlayer.getLang()).giveItems(p, main.getSettings().getString("Inventory.Lobby"), false);
        main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), Enums.ScoreboardType.LOBBY, false, false, false, false);


        if (gamePlayer.getHolograms() != null) gamePlayer.getHolograms().reloadHologram();
    }
}
