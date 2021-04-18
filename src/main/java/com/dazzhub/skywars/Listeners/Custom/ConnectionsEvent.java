package com.dazzhub.skywars.Listeners.Custom;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ConnectionsEvent extends Event
{
    private static HandlerList handlerList;

    private final GamePlayer gamePlayer;

    public ConnectionsEvent(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public static HandlerList getHandlerList() {
        return ConnectionsEvent.handlerList;
    }

    public HandlerList getHandlers() {
        return ConnectionsEvent.handlerList;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    static {
        ConnectionsEvent.handlerList = new HandlerList();
    }
}
