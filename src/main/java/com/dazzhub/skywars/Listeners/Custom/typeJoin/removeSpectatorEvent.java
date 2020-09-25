package com.dazzhub.skywars.Listeners.Custom.typeJoin;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class removeSpectatorEvent extends Event
{
    private static HandlerList handlerList;
    private GamePlayer gamePlayer;
    private Arena arena;
    private boolean goLobby;

    public removeSpectatorEvent(GamePlayer gamePlayer, boolean goLobby, Arena arena) {
        this.gamePlayer = gamePlayer;
        this.arena = arena;
        this.goLobby = goLobby;
    }

    public static HandlerList getHandlerList() {
        return removeSpectatorEvent.handlerList;
    }

    public HandlerList getHandlers() {
        return removeSpectatorEvent.handlerList;
    }

    static {
        removeSpectatorEvent.handlerList = new HandlerList();
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

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public boolean isGoLobby() {
        return goLobby;
    }

    public void setGoLobby(boolean goLobby) {
        this.goLobby = goLobby;
    }
}

