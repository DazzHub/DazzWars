package com.dazzhub.skywars.Listeners.Custom.typeJoin;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class addSpectatorEvent extends Event
{
    private static HandlerList handlerList;
    private GamePlayer gamePlayer;
    private Arena arena;

    public addSpectatorEvent(GamePlayer gamePlayer, Arena arena) {
        this.gamePlayer = gamePlayer;
        this.arena = arena;
    }

    public static HandlerList getHandlerList() {
        return addSpectatorEvent.handlerList;
    }

    public HandlerList getHandlers() {
        return addSpectatorEvent.handlerList;
    }

    static {
        addSpectatorEvent.handlerList = new HandlerList();
    }
}

