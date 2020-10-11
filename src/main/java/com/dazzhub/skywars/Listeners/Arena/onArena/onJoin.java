package com.dazzhub.skywars.Listeners.Arena.onArena;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.JoinEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Party.Party;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class onJoin implements Listener {

    private Main main;

    public onJoin(Main main) {
        this.main = main;
    }

    @EventHandler
    public void JoinEvent(JoinEvent e) {
        Player p = e.getPlayer();
        Arena arena = e.getArena();

        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
        Party party = gamePlayer.getParty();

        if (party == null) {
            if (arena.checkUsable()) {
                arena.addPlayer(gamePlayer);
            } else {
                if (!p.hasPermission("skywars.spectate")) {
                    if (arena.getGameStatus().equals(Enums.GameStatus.INGAME)) {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.inGame", "Error Messages.inGame"));
                    } else if (arena.getPlayers().size() == arena.getMaxPlayers()) {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Full", "Error Messages.Full"));
                    }
                } else {
                    if (!arena.getGameStatus().equals(Enums.GameStatus.RESTARTING)) {
                        Bukkit.getScheduler().runTask(this.main, () -> arena.addSpectator(gamePlayer));
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.JoinSpectator", "Error Messages.JoinSpectator"));
                    } else {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Arena-Not-Available", "Error Messages.Arena-Not-Available"));
                    }
                }
            }
        } else {
            if (arena.checkUsable()) {
                if (party.getOwner().equals(gamePlayer)) {
                    party.joinArena(gamePlayer, arena);
                } else {
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.JoinArenaNoOwner", "Error Party.JoinArenaNoOwner"));
                }
            }
        }
    }
}
