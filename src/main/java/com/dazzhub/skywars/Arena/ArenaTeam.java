package com.dazzhub.skywars.Arena;

import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArenaTeam
{
    private Arena arena;
    private Location spawn;
    private List<GamePlayer> members;

    public ArenaTeam(Arena arena, Location location) {
        this.members = new ArrayList<>();
        this.arena = arena;
        this.spawn = location;
    }

    public boolean isFull() {
        return this.members.size() >= arena.getMode().getSize();
    }

    public boolean hasPlayer(GamePlayer gamePlayer) {
        return this.members.contains(gamePlayer);
    }

    public void addPlayer(GamePlayer gamePlayer) {
        if (this.isFull() || hasPlayer(gamePlayer)) {
            return;
        }

        this.members.add(gamePlayer);
        gamePlayer.setArenaTeam(this);
    }


    public void removeTeam(GamePlayer gamePlayer) {
        if (gamePlayer.getArenaTeam() != null && this.members.contains(gamePlayer)) {
            gamePlayer.setArenaTeam(null);
            this.members.remove(gamePlayer);
        }
    }

    public List<GamePlayer> getAliveTeams() {
        return new ArrayList<>(this.members);
    }

    public List<GamePlayer> getMembers() {
        return members;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public Location getSpawn() {
        return spawn;
    }

}

