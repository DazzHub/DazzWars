package com.dazzhub.skywars.Listeners.Custom;

import com.dazzhub.skywars.Arena.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WinEvent extends Event
{
    private static HandlerList handlerList;
    private Arena arena;

    public WinEvent(Arena arena) {
        this.arena = arena;
    }

    public static HandlerList getHandlerList() {
        return WinEvent.handlerList;
    }

    public Arena getArena() {
        return this.arena;
    }

    public HandlerList getHandlers() {
        return WinEvent.handlerList;
    }

    static {
        WinEvent.handlerList = new HandlerList();
    }
}
