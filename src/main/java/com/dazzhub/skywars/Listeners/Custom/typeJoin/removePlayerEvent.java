package com.dazzhub.skywars.Listeners.Custom.typeJoin;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class removePlayerEvent extends Event
{
    private static HandlerList handlerList;
    private GamePlayer gamePlayer;
    private Arena arena;

    public removePlayerEvent(GamePlayer gamePlayer, Arena arena) {
        this.gamePlayer = gamePlayer;
        this.arena = arena;
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

    public void setArena(Arena arena) {
        this.arena = arena;
    }
}

