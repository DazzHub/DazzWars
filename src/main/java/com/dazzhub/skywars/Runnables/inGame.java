package com.dazzhub.skywars.Runnables;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.WinEvent;
import com.dazzhub.skywars.Utils.Runnable.utils.SnakeRunnableSync;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class inGame extends SnakeRunnableSync {

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
    public void onTick() {

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
            arena.setRefillGame(arena.RefillGame(nextRefill));

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
