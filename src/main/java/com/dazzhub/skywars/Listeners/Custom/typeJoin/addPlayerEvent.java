package com.dazzhub.skywars.Listeners.Custom.typeJoin;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class addPlayerEvent extends Event
{
    private static HandlerList handlerList;
    private GamePlayer gamePlayer;
    private Arena arena;

    public addPlayerEvent(GamePlayer gamePlayer, Arena arena) {
        this.gamePlayer = gamePlayer;
        this.arena = arena;
    }

    public static HandlerList getHandlerList() {
        return addPlayerEvent.handlerList;
    }

    public HandlerList getHandlers() {
        return addPlayerEvent.handlerList;
    }

    static {
        addPlayerEvent.handlerList = new HandlerList();
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

