package com.dazzhub.skywars.MySQL.utils;

import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class TaggedCooldown {

    private final HashMap<UUID, UUID> playerTagged;
    private final HashMap<UUID, Long> playerTagCooldown;

    public TaggedCooldown() {
        this.playerTagged = Maps.newHashMap();
        this.playerTagCooldown = Maps.newHashMap();
    }

    public void checkTimer(Player online) {
        if (!isCooldown(online)) {
            this.playerTagCooldown.remove(online.getUniqueId());
            this.playerTagged.remove(online.getUniqueId());
        }
    }

    public boolean isCooldown(Player player) {
        boolean playerExist = this.playerTagCooldown.containsKey(player.getUniqueId());

        if (!playerExist) return false;

        long cachedTime = this.playerTagCooldown.get(player.getUniqueId());
        long endTime = System.currentTimeMillis();

        long secondsElapsed = (endTime - cachedTime) / 1000L;

        return secondsElapsed < 15;
    }

    public boolean isTagged(Player damaged) {
        return this.getPlayerTagged().containsValue(damaged.getUniqueId());
    }

    public UUID getKiller(Player damaged) {
        UUID uuid = damaged.getUniqueId();

        return this.getPlayerTagged()
                .entrySet()
                .stream()
                .filter(entry -> uuid.equals(entry.getValue()))
                .map(Map.Entry::getKey).findFirst().orElse(null);
    }

    public void addTime(Player player, long time) {
        if (this.isCooldown(player)) this.getPlayerTagCooldown().replace(player.getUniqueId(), time);
        else this.getPlayerTagCooldown().put(player.getUniqueId(), time);
    }

    public void removeDeath(Player death) {
        this.playerTagged.remove(death.getUniqueId());
    }

    public void removeTimer(Player killer) {
        this.playerTagged.remove(killer.getUniqueId());
        this.playerTagCooldown.remove(killer.getUniqueId());
    }

    public void addTagged(Player damager, Player damaged) {
        if (!this.getPlayerTagged().containsKey(damager.getUniqueId())) {
            this.getPlayerTagged().put(damager.getUniqueId(), damaged.getUniqueId());
        } else {
            this.getPlayerTagged().replace(damager.getUniqueId(), damaged.getUniqueId());
        }
    }

}
