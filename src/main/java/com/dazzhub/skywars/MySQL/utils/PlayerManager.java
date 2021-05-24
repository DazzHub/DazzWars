package com.dazzhub.skywars.MySQL.utils;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private HashMap<UUID, GamePlayer> gamePlayers;
    private TaggedCooldown taggedCooldown;

    public PlayerManager() {
        this.gamePlayers = new HashMap<>();
        this.taggedCooldown = new TaggedCooldown();
    }

    public void addPlayer(UUID uuid, GamePlayer gamePlayer) {
        if (!this.containsPlayer(uuid)) {
            this.gamePlayers.put(uuid, gamePlayer);
        }
    }

    public void removePlayer(UUID uuid) {
        this.gamePlayers.remove(uuid);
    }

    public boolean containsPlayer(UUID uuid) {
        return this.gamePlayers.containsKey(uuid);
    }

    public GamePlayer getPlayer(UUID uuid) {
        if (this.containsPlayer(uuid)) {
            return this.gamePlayers.get(uuid);
        } else {
            return null;
        }
    }

    public TaggedCooldown getTaggedCooldown() {
        return taggedCooldown;
    }

    public HashMap<UUID, GamePlayer> getGamePlayers() {
        return gamePlayers;
    }
}