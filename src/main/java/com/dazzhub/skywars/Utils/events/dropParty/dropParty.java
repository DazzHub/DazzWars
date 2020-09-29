package com.dazzhub.skywars.Utils.events.dropParty;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Tools;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class dropParty implements eventParty {

    private Main main;

    private Arena arena;

    private int task;
    private int timer;
    private int drops;

    public dropParty(Arena arena) {
        this.main = Main.getPlugin();

        this.arena = arena;
        this.timer = arena.getArenac().getInt("Arena.dropparty.TimeStarting");
        this.drops = arena.getArenac().getInt("Arena.dropparty.TimeDrop");
    }

    @Override
    public void startEvent() {
        task = new BukkitRunnable() {
            @Override
            public void run() {

                arena.getPlayers().forEach(gamePlayer -> {
                    Set<String> keys = gamePlayer.getLangMessage().getConfigurationSection("Messages.TypeEvent.DropParty.Starting").getKeys(false);
                    keys.stream().filter(time_config -> timer == Integer.parseInt(time_config)).forEach(time_config -> gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TypeEvent.DropParty.Starting." + time_config).replaceAll("%seconds%", String.valueOf(timer))));
                });

                if (timer <= 1){
                    this.cancel();
                    startDrops();
                }

                timer--;
            }
        }.runTaskTimerAsynchronously(main,0,20).getTaskId();
    }

    private void startDrops(){

        task = new BukkitRunnable() {
            @Override
            public void run() {

                Tools.spawnEntities(arena.getSpawnSpectator(), null, main.getChestManager().getChestHashMap().get(arena.getArenac().getString("Arena.dropparty.Chest")).getRandomItems(), arena.getArenac().getInt("Arena.dropparty.AmountDrop"), arena.getArenac().getInt("Arena.dropparty.Radio"), false, false);

                if (drops <= 1){
                    this.cancel();
                }

                drops--;
            }
        }.runTaskTimer(main,0,20).getTaskId();
    }

    @Override
    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(task);
        task = 0;
    }
}
