package com.dazzhub.skywars.Listeners.Custom;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

@Getter
public class PlayerDamageByPlayerEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player damager;
    private final Player damaged;
    private final EntityDamageEvent.DamageCause damageCause;

    @Getter(AccessLevel.NONE)
    private boolean isCancelled;

    @Setter
    private double damage;

    public PlayerDamageByPlayerEvent(Player damager, Player damaged, double damage, EntityDamageEvent.DamageCause damageCause) {
        this.damager = damager;
        this.damaged = damaged;
        this.damage = damage;
        this.damageCause = damageCause;
        this.isCancelled = false;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
