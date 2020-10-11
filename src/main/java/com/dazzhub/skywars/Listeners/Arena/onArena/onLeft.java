package com.dazzhub.skywars.Listeners.Arena.onArena;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.JoinEvent;
import com.dazzhub.skywars.Listeners.Custom.LeftEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Party.Party;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class onLeft implements Listener {

    private Main main;

    public onLeft(Main main) {
        this.main = main;
    }

    @EventHandler
    public void JoinEvent(LeftEvent e) {
        Player p = e.getPlayer();
        Arena arena = e.getArena();

        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
        Party party = gamePlayer.getParty();

        switch (e.getLeftCause()){
            case SPECTATOR:{
                if (party == null) {
                    arena.removeSpectator(gamePlayer,false);
                } else {
                    if (party.getOwner().equals(gamePlayer)) {

                        //arena.removeSpectator(gamePlayer,false);
                        party.getMembers().forEach(gamePlayer1 -> arena.removeSpectator(gamePlayer1, false));

                    } else {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.JoinArenaNoOwner", "Error Party.JoinArenaNoOwner"));
                    }
                }
                break;
            }

            case INTERACTSPECTATOR:
            case DISCONNECTSPECTATOR:{
                arena.removeSpectator(gamePlayer, true);
                break;
            }

            default:{
                if (party == null) {
                    arena.removePlayer(gamePlayer);
                } else {
                    if (party.getOwner().equals(gamePlayer)) {
                        party.getMembers().forEach(arena::removePlayer);
                    } else {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.JoinArenaNoOwner", "Error Party.JoinArenaNoOwner"));
                    }
                }
                break;
            }
        }

    }
}
