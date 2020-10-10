package com.dazzhub.skywars.Listeners.Arena.onSettings;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.typeJoin.addPlayerEvent;
import com.dazzhub.skywars.Listeners.Custom.typeJoin.addSpectatorEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.Tools;
import com.dazzhub.skywars.Utils.scoreboard.ScoreBoardAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class addSpectator implements Listener {

    private Main main;

    public addSpectator(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onAddSpectator(addSpectatorEvent e) {
        GamePlayer gamePlayer = e.getGamePlayer();
        Arena arena = e.getArena();

        Player p = gamePlayer.getPlayer();

        if (gamePlayer.getArenaTeam() != null) {
            gamePlayer.getArenaTeam().removeTeam(gamePlayer);
        }

        gamePlayer.setLobby(false);
        gamePlayer.setArena(arena);
        arena.getPlayers().remove(gamePlayer);
        arena.getSpectators().add(gamePlayer);

        arena.getPlayers().forEach(online -> Tools.HidePlayer(online.getPlayer(), p));

        gamePlayer.addSpectating();
        main.getItemManager().giveItems(p, main.getSettings().getString("Inventory.Arena.Spectator"), false);

        gamePlayer.getPlayer().teleport(arena.getSpawnSpectator());

        main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), Enums.ScoreboardType.SPECTATOR, false, true, true, false);

        gamePlayer.setSpectating(true);
    }
}
