package com.dazzhub.skywars.Runnables;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.cryptomorin.xseries.messages.Titles;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class RefillGame extends BukkitRunnable {

    private Arena arena;
    private int timer;

    public RefillGame(Arena arena, int timer){
        this.arena = arena;
        this.timer = timer;
    }

    @Override
    public void run() {

        if (timer <= 1){
            this.cancel();

            for (GamePlayer p : arena.getPlayers()) {

                Titles.sendTitle(p.getPlayer(),
                        p.getLangMessage().getInt("Messages.RefillTitle.Fade",20),
                        p.getLangMessage().getInt("Messages.RefillTitle.Stay",20),
                        p.getLangMessage().getInt("Messages.RefillTitle.Out",20),
                        c(p.getLangMessage().getString("Messages.RefillTitle.Info", "Error title refill").split(";")[0]).replace("%player%", p.getPlayer().getName()),
                        c(p.getLangMessage().getString("Messages.RefillTitle.Info", "Error subtitle refill").split(";")[1]).replace("%player%", p.getPlayer().getName()));

                p.sendMessage(c(p.getLangMessage().getString("Messages.RefillTitle.ChatAlert", "Error RefillTitle.ChatAlert")));
            }

            Bukkit.getScheduler().runTask(Main.getPlugin(), () -> arena.fillChests());
            this.arena.setRefillGame(null);
        }

        timer--;
    }


    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
