package com.dazzhub.skywars.Listeners.Arena.onArena;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.JoinEvent;
import com.dazzhub.skywars.Listeners.Custom.LeftEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
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

        switch (e.getLeftCause()){
            case SPECTATOR:{
                arena.removeSpectator(gamePlayer, false);
                break;
            }

            case INTERACTSPECTATOR:
            case DISCONNECTSPECTATOR:{
                arena.removeSpectator(gamePlayer, true);
                break;
            }

            default:{
                arena.removePlayer(gamePlayer);
                break;
            }
        }

    }
}
