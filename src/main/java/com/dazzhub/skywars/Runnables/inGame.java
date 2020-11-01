package com.dazzhub.skywars.Runnables;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.WinEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.cryptomorin.xseries.messages.Titles;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@Getter
public class inGame extends BukkitRunnable {

    private Arena arena;
    private int timer;

    private int falldamage;
    private int nextRefill;

    public inGame(Arena arena) {
        this.arena = arena;
        this.timer = arena.getDurationGame();
        this.falldamage = 5;
    }

    @Override
    public void run() {

        if (this.arena.getAliveTeams().size() <= 1) {
            this.cancel();
            Bukkit.getPluginManager().callEvent(new WinEvent(this.arena));
        }

        if (this.arena.getPlayers().isEmpty()) {
            this.cancel();
            Bukkit.getPluginManager().callEvent(new WinEvent(this.arena));
        }

        this.arena.getIHoloChest().updateHolo();

        nextRefill = arena.getHighest(arena.getRefillTime(), timer);

        if (arena.getRefillGame() == null && !arena.getRefillTime().isEmpty()) {
            arena.setRefillGame(new RefillGame(arena, nextRefill));
            arena.getRefillGame().runTaskTimerAsynchronously(Main.getPlugin(),0,20);
            arena.getRefillTime().remove((Integer) nextRefill);
        }

        if (timer <= 1) {
            this.cancel();
            Bukkit.getPluginManager().callEvent(new WinEvent(this.arena));

            arena.getPlayers().forEach(p -> p.sendMessage(p.getLangMessage().getString("Messages.EndTime", "Error Messages.EndTime")));
            arena.getSpectators().forEach(p -> p.sendMessage(p.getLangMessage().getString("Messages.EndTime", "Error Messages.EndTime")));
        }

        if (falldamage <= 1) {
            arena.setDamageFallStarting(false);
        }

        falldamage--;
        timer--;
    }

}
