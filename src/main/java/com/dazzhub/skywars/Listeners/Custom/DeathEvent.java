package com.dazzhub.skywars.Listeners.Custom;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent extends Event {

    private static HandlerList handlers;
    private GamePlayer dead;
    private GamePlayer killer;
    private PlayerDeathEvent event;
    private Arena arena;

    public DeathEvent(GamePlayer dead, GamePlayer killer, Arena arena, PlayerDeathEvent event) {
        this.dead = dead;
        this.killer = killer;
        this.arena = arena;
        this.event = event;
    }

    public HandlerList getHandlers() {
        return DeathEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return DeathEvent.handlers;
    }

    public GamePlayer getDead() {
        return this.dead;
    }

    public GamePlayer getKiller() {
        return this.killer;
    }

    public PlayerDeathEvent getEvent() {
        return this.event;
    }

    public Arena getArena() {
        return this.arena;
    }

    static {
        handlers = new HandlerList();
    }

}
