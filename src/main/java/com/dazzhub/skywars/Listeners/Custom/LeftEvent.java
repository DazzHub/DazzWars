package com.dazzhub.skywars.Listeners.Custom;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LeftEvent extends Event
{
    private static HandlerList handlerList;
    private Player player;
    private Arena arena;
    private Enums.LeftCause leftCause;

    public LeftEvent(Player player, Arena arena, Enums.LeftCause leftCause) {
        this.player = player;
        this.arena = arena;
        this.leftCause = leftCause;
    }

    public HandlerList getHandlers() {
        return LeftEvent.handlerList;
    }

    public static HandlerList getHandlerList() {
        return LeftEvent.handlerList;
    }

    public Enums.LeftCause getLeftCause() {
        return this.leftCause;
    }

    public Arena getArena() {
        return this.arena;
    }

    public Player getPlayer() {
        return this.player;
    }

    static {
        LeftEvent.handlerList = new HandlerList();
    }
}

