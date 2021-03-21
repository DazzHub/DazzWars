package com.dazzhub.skywars.Listeners.Custom.typeJoin;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class removePlayerEvent extends Event
{
    private static HandlerList handlerList;

    private GamePlayer gamePlayer;
    private Arena arena;
    private boolean goLobby;

    public removePlayerEvent(GamePlayer gamePlayer, Arena arena, boolean goLobby) {
        this.gamePlayer = gamePlayer;
        this.arena = arena;
        this.goLobby = goLobby;
    }

    public static HandlerList getHandlerList() {
        return removePlayerEvent.handlerList;
    }

    public HandlerList getHandlers() {
        return removePlayerEvent.handlerList;
    }

    static {
        removePlayerEvent.handlerList = new HandlerList();
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Arena getArena() {
        return arena;
    }

    public boolean isGoLobby() {
        return goLobby;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }
}

