package com.dazzhub.skywars.Listeners.Custom;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JoinEvent extends Event
{
    private static HandlerList handlerList;
    private Player player;
    private Arena arena;
    private Enums.JoinCause joinCause;

    public JoinEvent(Player player, Arena arena, Enums.JoinCause joinCause) {
        this.player = player;
        this.arena = arena;
        this.joinCause = joinCause;
    }

    public static HandlerList getHandlerList() {
        return JoinEvent.handlerList;
    }

    public HandlerList getHandlers() {
        return JoinEvent.handlerList;
    }

    public Enums.JoinCause getJoinCause() {
        return this.joinCause;
    }

    public Arena getArena() {
        return this.arena;
    }

    public Player getPlayer() {
        return this.player;
    }

    static {
        JoinEvent.handlerList = new HandlerList();
    }
}
